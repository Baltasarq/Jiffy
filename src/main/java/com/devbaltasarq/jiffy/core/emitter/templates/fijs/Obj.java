// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates.fijs;


import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.emitter.templates.Templater;

import java.util.HashMap;
import java.util.Map;


/** Templates for objs in fi-js. */
public class Obj extends Templater {
    private static final String VAR_OBJ_NAME = "$(OBJ_VAR_NAME)";
    private static final String VAR_OBJ_ID = "$(OBJ_ID)";
    private static final String VAR_OBJ_OWNER = "$(OBJ_LOC_OWNER)";
    private static final String VAR_OBJ_DESC = "$(OBJ_DESC)";
    private static final String VAR_OBJ_PORTABLE = "$(OBJ_PORTABLE)";
    private static final String VAR_OBJ_SYN_LIST = "$(OBJ_SYN_LIST)";
    
    public Obj(final Entity ENT)
    {
        super( ENT );
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final var ENTITY_OBJ = (com.devbaltasarq.jiffy.core.ast.Obj) this.getEntity();
        final String LOC_OWNER = Id.varNameFromId(
                                    "LOC",
                                     ENTITY_OBJ.getOwner().getId().get() );
        final String LOC_ID = Id.varNameFromId( "OBJ", ENTITY_OBJ.getId().get() );

        SUBSTS.putAll(
                Map.of(
                        VAR_OBJ_NAME, LOC_ID,
                        VAR_OBJ_ID, ENTITY_OBJ.getId().get(),
                        VAR_OBJ_DESC, ENTITY_OBJ.getDesc(),
                        VAR_OBJ_OWNER, LOC_OWNER ));

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts( OBJ_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        DEFAULT_SUBSTS.clear();
        DEFAULT_SUBSTS.putAll(
                        Map.of(
                                VAR_OBJ_SYN_LIST, "",
                                VAR_OBJ_PORTABLE, "Ent.Scenery" ));
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();;
    private static final String OBJ_TEMPLATE = """
                
    const %s = ctrl.creaObj(
        "%s",
        [ %s ],
        "%s",
        %s,
        %s
    );
    """.formatted(
        VAR_OBJ_NAME,
        VAR_OBJ_ID,
        VAR_OBJ_SYN_LIST,
        VAR_OBJ_DESC,
        VAR_OBJ_OWNER,
        VAR_OBJ_PORTABLE
        );
}
