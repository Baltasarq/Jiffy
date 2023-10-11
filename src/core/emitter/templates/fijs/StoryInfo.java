// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.emitter.templates.fijs;


import core.AST;
import core.ast.Loc;
import core.emitter.templates.Templater;
import core.parser.RValue;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/** Templates for locs in fi-js. */
public class StoryInfo extends Templater {
    public StoryInfo(final AST AST)
    {
        super( AST.getStory() );
        this.AST = AST;
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final core.ast.Story ENT_STORY = (core.ast.Story) this.getEntity();
        final DateTimeFormatter DT_FMT = DateTimeFormatter
                            .ofPattern("uuuuMMdd", Locale.ROOT );
        final String FORMATTED_DATE = DT_FMT.format( ZonedDateTime.now() );

        SUBSTS.put( "$STORY_ID", ENT_STORY.getId().get() );
        SUBSTS.put( "$STORY_DESC", ENT_STORY.getDesc() );
        SUBSTS.put( "$STORY_VERSION",
                    DEFAULT_SUBSTS.get( "$STORY_VERSION" )
                            + " " + FORMATTED_DATE );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( STORY_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$STORY_AUTHOR",
                "$STORY_PIC",
                "$STORY_INTRO",
                "$STORY_DESC",
                "$STORY_VERSION",
                "$STORY_START_LOC"
        };
        final String[] VALUES = {
                /* $STORY_AUTHOR     <- */ "author@brave.authors.com",
                /* $STORY_PIC        <- */ "",
                /* $STORY_INTRO      <- */ "",
                /* $STORY_DESC       <- */ "",
                /* $STORY_VERSION    <- */ "1.0",
                /* $STORY_START_LOC  <- */ "null"
        };

        // Create default substs
        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        // Real values, if present as vbles
        final RValue VAR_AUTHOR = this.getVar( "author" );
        final RValue VAR_PIC = this.getVar( "pic" );
        final RValue VAR_INTRO = this.getVar( "intro" );
        final RValue VAR_VERSION = this.getVar( "version" );
        final RValue VAR_START_LOC = this.getVar( "start" );

        if ( VAR_AUTHOR != null ) {
            DEFAULT_SUBSTS.put( "$STORY_AUTHOR", VAR_AUTHOR.toString() );
        }

        if ( VAR_PIC != null ) {
            DEFAULT_SUBSTS.put( "$STORY_PIC", VAR_PIC.toString() );
        }

        if ( VAR_INTRO != null ) {
            DEFAULT_SUBSTS.put( "$STORY_INTRO", VAR_INTRO.toString() );
        }

        if ( VAR_VERSION != null ) {
            DEFAULT_SUBSTS.put( "$STORY_VERSION", VAR_VERSION.toString() );
        }

        if ( VAR_START_LOC != null ) {
            final Loc LOC = this.AST.findLocById( VAR_START_LOC.toString() );

            if ( LOC != null ) {
                DEFAULT_SUBSTS.put( "$STORY_START_LOC", VAR_START_LOC.toString() );
            }
        }

        return;
    }

    private final AST AST;

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();

    private static final String STORY_TEMPLATE = """
            
            
            // ------------------------------------------------------- Player ---
            const PLAYER = ctrl.personas.creaPersona(
                "Jugador",
                ["jugador", "jugadora" ],
                "El bravo PC.",
                $STORY_START_LOC
            );
            
            
            // ---------------------------------------------------------- Ini ---
            ctrl.ini = function() {
                this.setTitle( "$STORY_ID" );
                this.setIntro( "$STORY_DESC" );
                this.setPic( "$STORY_PIC" );
                this.setAuthor( "$STORY_AUTHOR" );
                this.setVersion( "$STORY_VERSION" );
                this.personas.changePlayer( PLAYER );
                this.locs.setStart( $STORY_START_LOC );
            };
            """;
}
