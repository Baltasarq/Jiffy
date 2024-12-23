// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates.fijs;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.emitter.templates.Templater;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/** Templates for locs in fi-js. */
public class StoryCommentHeader extends Templater {
    public StoryCommentHeader(final Entity ENT)
    {
        super( ENT );
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final var ENT_STORY = (com.devbaltasarq.jiffy.core.ast.Story) this.getEntity();
        final DateTimeFormatter DT_FMT = DateTimeFormatter
                .ofPattern(
                        "uuuu-MM-dd' 'HH:mm",
                        Locale.ROOT );
        final String FORMATTED_DATE = DT_FMT.format( ZonedDateTime.now() );


        SUBSTS.put( "$STORY_ID", ENT_STORY.getId().get() );
        SUBSTS.put( "$STORY_DESC", ENT_STORY.getDesc() );
        SUBSTS.put( "$STORY_DATE", FORMATTED_DATE );
        SUBSTS.put( "$STORY_IFID", ENT_STORY.getIfId().toString() );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( STORY_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$STORY_DATE",
                "$STORY_IFID",
        };
        final String[] VALUES = {
                /* $STORY_DATE       <- */ "[ERR] DATE?",
                /* $STORY_IFID       <- */ "[ERR] IFID?",
        };

        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        return;
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();

    private static final String STORY_TEMPLATE = """
    // $STORY_ID
    /*
        $STORY_DESC
        
        @ $STORY_DATE
        IfId $STORY_IFID
    */
    """;
}
