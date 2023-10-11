// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.emitter.templates.fijs;


import core.Util;
import core.ast.Entity;
import core.emitter.templates.Templater;
import core.parser.RValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Templates for locs in fi-js. */
public class Loc extends Templater {
    public Loc(final Entity ENT)
    {
        super( ENT );
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final core.ast.Loc ENTITY_LOC = (core.ast.Loc) this.getEntity();

        SUBSTS.put( "$LOC_VAR_NAME", Util.varNameFromId( "LOC", ENTITY_LOC.getId().get() ) );
        SUBSTS.put( "$LOC_ID", ENTITY_LOC.getId().get() );
        SUBSTS.put( "$LOC_DESC", ENTITY_LOC.getDesc() );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( LOC_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$LOC_TITLE",
                "$LOC_SYN_LIST",
                "$LOC_PIC_FILE",
        };
        final String[] VALUES = {
                /* $LOC_TITLE        <- */ "title",
                /* $LOC_SYN_LIST     <- */ "",
                /* $LOC_PIC_FILE     <- */ "",
        };

        // Create base substitutions
        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        // Add pic
        final RValue PIC_VALUE = this.getVar( "pic" );

        if ( PIC_VALUE != null ) {
            DEFAULT_SUBSTS.put( "$LOC_PIC_FILE", PIC_VALUE.toString() );
        }

        // Add title
        final core.ast.Loc LOC = (core.ast.Loc) this.getEntity();
        DEFAULT_SUBSTS.put( "$LOC_TITLE", LOC.getTitle() );

        this.addSyns( DEFAULT_SUBSTS );
    }

    private void addSyns(final Map<String, String> DEFAULT_SUBSTS)
    {
        final List<String> SYNS = this.getEntity().getySyns();

        if ( !SYNS.isEmpty() ) {
            // Add quotes
            SYNS.replaceAll( (s) -> "\"" + s + "\"" );
            SYNS.add( 0, "\"" + this.getEntity().getId().get() + "\"" );
            DEFAULT_SUBSTS.put( "$LOC_SYN_LIST", String.join( ", ", SYNS ) );
        }

        return;
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();;
    private static final String LOC_TEMPLATE = """
    
    
    // -------------------------------------------------- $LOC_ID ---
    const $LOC_VAR_NAME = ctrl.locs.crea(
        "$LOC_TITLE",
        [ $LOC_SYN_LIST ],
        "$LOC_DESC" );
                            
    $LOC_VAR_NAME.ini = function() {
        this.pic = "$LOC_PIC_FILE";
    };
    """;
}
