// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates.fijs;


import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.ast.Obj;
import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.ast.Direction;
import com.devbaltasarq.jiffy.core.emitter.templates.Templater;
import com.devbaltasarq.jiffy.core.errors.EmitError;
import com.devbaltasarq.jiffy.core.parser.RValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Templates for locs in fi-js. */
public class Loc extends Templater {
    private static final String EXITS_PLACEHOLDER = "%EXITS%";
    private static final String OBJS_PLACEHOLDER = "%OBJS%";
    private static final String VAR_LOC_NAME = "$(LOC_VAR_NAME)";
    private static final String VAR_LOC_ID = "$(LOC_ID)";
    private static final String VAR_LOC_DESC = "$(LOC_DESC)";
    private static final String VAR_EXIT_DIR = "$(EXIT_DIR";
    private static final String VAR_LOC_DEST = "$(EXIT_LOC_DEST";
    private static final String VAR_LOC_TITLE = "$(LOC_TITLE)";
    private static final String VAR_LOC_SYN_LIST = "$(LOC_SYN_LIST)";
    private static final String VAR_LOC_PIC_FILE = "$(LOC_PIC_FILE)";
    
    public Loc(final Entity ENT)
    {
        super( ENT );
        this.initDefaultSubsts();
    }
    
    private String substExits(final List<Direction> DIRECTIONS,
                              final Map<String, String> SUBSTS)
                    throws EmitError
    {
        final StringBuffer TORET = new StringBuffer();
        final var LOC = (com.devbaltasarq.jiffy.core.ast.Loc) this.getEntity();
        final AST THE_AST = this.getEntity().getAST();
        
        for(int i = 0; i < DIRECTIONS.size(); ++i) {
            final Direction DIR = DIRECTIONS.get( i );
            final String DIR_VBLE = VAR_EXIT_DIR + ( i + 1 ) + ")";
            final String LOC_VBLE = VAR_LOC_DEST + ( i + 1 ) + ")";
            final var ID_LOC_DEST = LOC.getExitAt( DIR );
            final var LOC_DEST = THE_AST.findLocById( ID_LOC_DEST );
            
            if ( LOC_DEST != null ) {
                final String LOC_VAR_NAME = Id.varNameFromId( "LOC", LOC_DEST.getId().get() );

                TORET.append(
                        String.format(
                            "        this.setExit( \"%s\", %s );\n",
                            DIR_VBLE, LOC_VBLE ));
                SUBSTS.put( DIR_VBLE, DIR.toString().toLowerCase() );
                SUBSTS.put( LOC_VBLE, LOC_VAR_NAME );
            } else {
                throw new EmitError( LOC.toString(),
                                        String.format(
                                            "not found \"%s\" at exit \"%s\"",
                                                  ID_LOC_DEST,
                                                  DIR ) );
            }
        }
        
        return TORET.toString();
    }
    
    private String substObjs(final List<Obj> OBJS)
    {
        final StringBuffer TORET = new StringBuffer();
        
        for(Obj entObj: OBJS) {
            var obj =
                    new com.devbaltasarq.jiffy.core.emitter.templates.fijs.Obj( entObj );
            
            TORET.append( obj.subst() );
        }
        
        return TORET.toString();
    }

    @Override
    public String subst() throws EmitError
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final var ENTITY_LOC = (com.devbaltasarq.jiffy.core.ast.Loc) this.getEntity();
        final List<Direction> DIRECTIONS = ENTITY_LOC.getAllExitDirections();
        final List<Obj> OBJS = ENTITY_LOC.getObjs();
        final String LOC_ID = Id.varNameFromId( "LOC", ENTITY_LOC.getId().get() );
        String template;

        if ( !DIRECTIONS.isEmpty() ) {
            template = LOC_TEMPLATE.replace( EXITS_PLACEHOLDER,
                        this.substExits( DIRECTIONS, SUBSTS ));
        } else {
            template = LOC_TEMPLATE.replace( EXITS_PLACEHOLDER, "" );
        }

        if ( !OBJS.isEmpty() ) {
            template = template.replace( OBJS_PLACEHOLDER, this.substObjs( OBJS ) );
        } else {
            template = template.replace( OBJS_PLACEHOLDER, "" );
        }

        SUBSTS.putAll(
                Map.of(
                    VAR_LOC_NAME, LOC_ID,
                    VAR_LOC_ID, ENTITY_LOC.getId().get(),
                    VAR_LOC_DESC, ENTITY_LOC.getDesc() )
        );
        
        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( template, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        DEFAULT_SUBSTS.clear();
        DEFAULT_SUBSTS.putAll(
                        Map.of( VAR_LOC_TITLE, "title",
                                   VAR_LOC_SYN_LIST, "",
                                   VAR_LOC_PIC_FILE, "" ));

        // Add pic
        final RValue PIC_VALUE = this.getVar( "pic" );

        if ( PIC_VALUE != null ) {
            DEFAULT_SUBSTS.put( VAR_LOC_PIC_FILE, PIC_VALUE.toString() );
        }

        // Add title
        final var LOC = (com.devbaltasarq.jiffy.core.ast.Loc) this.getEntity();
        DEFAULT_SUBSTS.put( VAR_LOC_TITLE, LOC.getTitle() );

        this.addSyns( DEFAULT_SUBSTS );
    }

    private void addSyns(final Map<String, String> DEFAULT_SUBSTS)
    {
        final List<String> SYNS = this.getEntity().getSyns();

        if ( !SYNS.isEmpty() ) {
            // Add quotes
            SYNS.replaceAll( (s) -> "\"" + s + "\"" );
            SYNS.add( 0, "\"" + this.getEntity().getId().get() + "\"" );
            DEFAULT_SUBSTS.put( VAR_LOC_SYN_LIST, String.join( ", ", SYNS ) );
        }

        return;
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();;
    private static final String LOC_TEMPLATE = String.format( """
    
    
    // -------------------------------------------------- %s ---
    const %s = ctrl.locs.crea(
        "%s",
        [ "%s" ],
        "%s",
        function() {
            this.pic = "%s";
    %s
    %s    }
    );
    """,
    VAR_LOC_ID,
    VAR_LOC_NAME,
    VAR_LOC_TITLE,
    VAR_LOC_SYN_LIST,
    VAR_LOC_DESC,
    VAR_LOC_PIC_FILE,
    EXITS_PLACEHOLDER,
    OBJS_PLACEHOLDER );
}
