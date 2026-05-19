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
    private static final String VAR_STORY_ID = "$(STORY_ID)";
    private static final String VAR_STORY_DESC = "$(STORY_DESC)";
    private static final String VAR_STORY_DATE = "$(STORY_DATE)";
    private static final String VAR_STORY_IFID = "$(STORY_IFID)";
    
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

        SUBSTS.putAll(
                Map.of(
                    VAR_STORY_ID, ENT_STORY.getId().get(),
                    VAR_STORY_DESC, ENT_STORY.getDesc(),
                    VAR_STORY_DATE, FORMATTED_DATE,
                    VAR_STORY_IFID, ENT_STORY.getIfId().toString() ));

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( STORY_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        DEFAULT_SUBSTS.putAll(
                        Map.of(
                                VAR_STORY_DATE, "[ERR] DATE?",
                                VAR_STORY_IFID, "[ERR] IFID?" ));
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();

    private static final String STORY_TEMPLATE = """
    // $(STORY_ID)
    /*
        $(STORY_DESC)
        
        @ $(STORY_DATE)
        IfId $(STORY_IFID)
    */
    """;
}
