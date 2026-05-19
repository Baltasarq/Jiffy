// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates.fijs;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.emitter.templates.Templater;
import com.devbaltasarq.jiffy.core.parser.RValue;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


/** Templates for locs in fi-js. */
public class StoryInfo extends Templater {
    private static final String VAR_STORY_ID = "$(STORY_ID)";
    private static final String VAR_STORY_DESC = "$(STORY_DESC)";
    private static final String VAR_STORY_VERSION = "$(STORY_VERSION)";
    private static final String VAR_STORY_AUTHOR = "$(STORY_AUTHOR)";
    private static final String VAR_STORY_PIC = "$(STORY_PIC)";
    private static final String VAR_STORY_INTRO = "$(STORY_INTRO)";
    private static final String VAR_STORY_START_LOC = "$(STORY_START_LOC)";
    
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
        final var ENT_STORY = (com.devbaltasarq.jiffy.core.ast.Story) this.getEntity();
        final DateTimeFormatter DT_FMT = DateTimeFormatter
                            .ofPattern("uuuuMMdd", Locale.ROOT );
        final String FORMATTED_DATE = DT_FMT.format( ZonedDateTime.now() );

        SUBSTS.putAll(
                Map.of(
                        VAR_STORY_ID, ENT_STORY.getId().get(),
                        VAR_STORY_DESC, ENT_STORY.getDesc(),
                        VAR_STORY_VERSION,
                                        DEFAULT_SUBSTS.get( VAR_STORY_VERSION )
                                            + " " + FORMATTED_DATE
                        ));

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( STORY_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        // Initial values
        String author = "author@brave.authors.com";
        String pic = "";
        String desc = "";
        String intro = "";
        String version = "1.0";
        String startLoc = "null";
        
        // Real values, if present as vbles
        final RValue VAR_AUTHOR = this.getVar( "author" );
        final RValue VAR_PIC = this.getVar( "pic" );
        final RValue VAR_INTRO = this.getVar( "intro" );
        final RValue VAR_VERSION = this.getVar( "version" );
        final RValue VAR_START_LOC = this.getVar( "start" );

        if ( VAR_AUTHOR != null ) {
            author = VAR_AUTHOR.toString();
        }

        if ( VAR_PIC != null ) {
            pic = VAR_PIC.toString();
        }

        if ( VAR_INTRO != null ) {
            intro = VAR_INTRO.toString();
        }

        if ( VAR_VERSION != null ) {
            version = VAR_VERSION.toString();
        }

        if ( VAR_START_LOC != null ) {
            final var LOC = this.AST.findLocByStrId( VAR_START_LOC.toString() );

            if ( LOC != null ) {
                startLoc = Id.varNameFromId( "LOC", LOC.getId().toString() );
            }
        }

        DEFAULT_SUBSTS.putAll(
                        Map.of(
                            VAR_STORY_AUTHOR, author,
                            VAR_STORY_PIC, pic,
                            VAR_STORY_INTRO, intro,
                            VAR_STORY_DESC, desc,
                            VAR_STORY_VERSION, version,
                            VAR_STORY_START_LOC, startLoc ));
    }

    private final AST AST;

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();

    private static final String STORY_TEMPLATE = """
            
            
            // ------------------------------------------------------- Player ---
            const PLAYER = ctrl.personas.creaPersona(
                "Jugador",
                ["jugador", "jugadora" ],
                "El bravo PC.",
                %s
            );
            
            
            // ---------------------------------------------------------- Ini ---
            ctrl.ini = function() {
                this.setTitle( "%s" );
                this.setIntro( "%s" );
                this.setPic( "%s" );
                this.setAuthor( "%s" );
                this.setVersion( "%s" );
                this.personas.changePlayer( PLAYER );
                this.locs.setStart( %s );
            };
            """.formatted(
                    VAR_STORY_START_LOC,
                    VAR_STORY_ID,
                    VAR_STORY_DESC,
                    VAR_STORY_PIC,
                    VAR_STORY_AUTHOR,
                    VAR_STORY_VERSION,
                    VAR_STORY_START_LOC
            );
}
