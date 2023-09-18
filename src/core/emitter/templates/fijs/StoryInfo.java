// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.emitter.templates.fijs;


import core.Util;
import core.ast.Entity;
import core.emitter.templates.Templater;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/** Templates for locs in fi-js. */
public class StoryInfo extends Templater {
    public StoryInfo(final Entity ENT, final core.ast.Loc START)
    {
        super( ENT );
        this.START = START;
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
        final String LOC_ID = Util.varNameFromId( "LOC", this.START.getId().get() );

        SUBSTS.put( "$STORY_ID", ENT_STORY.getId().get() );
        SUBSTS.put( "$STORY_DESC", ENT_STORY.getDesc() );
        SUBSTS.put( "$STORY_VERSION", FORMATTED_DATE );
        SUBSTS.put( "$STORY_START_LOC", LOC_ID );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( STORY_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$STORY_DESC",
                "$STORY_VERSION",
                "$STORY_START_LOC"
        };
        final String[] VALUES = {
                /* $STORY_DESC       <- */ "",
                /* $STORY_VERSION    <- */ "1.0",
                /* $STORY_START_LOC  <- */ "null"
        };

        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        return;
    }

    private final core.ast.Loc START;

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
                // this.setPic( "res/portada.jpg" );
                this.setAuthor( "Increíble Autor" );
                this.setVersion( "$STORY_VERSION" );
                this.personas.changePlayer( PLAYER );
                this.locs.setStart( $STORY_START_LOC );
            };
            """;
}
