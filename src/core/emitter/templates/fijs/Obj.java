// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.emitter.templates.fijs;


import core.Util;
import core.ast.Entity;
import core.emitter.templates.Templater;

import java.util.HashMap;
import java.util.Map;


/** Templates for objs in fi-js. */
public class Obj extends Templater {
    public Obj(final Entity ENT)
    {
        super( ENT );
        this.initDefaultSubsts();
    }

    @Override
    public String subst()
    {
        final Map<String, String> SUBSTS = new HashMap<>();
        final core.ast.Obj ENTITY_OBJ = (core.ast.Obj) this.getEntity();
        final String LOC_OWNER = Util.varNameFromId(
                                    "LOC",
                                     ENTITY_OBJ.getOwner().getId().get() );

        SUBSTS.put( "$OBJ_VAR_NAME", Util.varNameFromId( "OBJ", ENTITY_OBJ.getId().get() ) );
        SUBSTS.put( "$OBJ_ID", ENTITY_OBJ.getId().get() );
        SUBSTS.put( "$OBJ_DESC", ENTITY_OBJ.getDesc() );
        SUBSTS.put( "$LOC_OWNER", LOC_OWNER );

        this.mergeSubstMaps( DEFAULT_SUBSTS, SUBSTS );
        return this.applySubsts(OBJ_TEMPLATE, SUBSTS );
    }

    private void initDefaultSubsts()
    {
        final String[] KEYS = {
                "$OBJ_SYN_LIST",
                "$OBJ_PORTABLE_OR_NOT",
        };
        final String[] VALUES = {
                /* OBJ_SYN_LIST             <- */ "",
                /* $OBJ_PORTABLE_OR_NOT     <- */ "Ent.Scenery",
        };

        DEFAULT_SUBSTS.clear();
        for(int i = 0; i < KEYS.length; ++i) {
            DEFAULT_SUBSTS.put( KEYS[ i ], VALUES[ i ] );
        }

        return;
    }

    private static final Map<String, String> DEFAULT_SUBSTS = new HashMap<>();;
    private static final String OBJ_TEMPLATE = """
                
    const $OBJ_VAR_NAME = ctrl.creaObj(
        "$OBJ_ID",
        [ $OBJ_SYN_LIST ],
        "$OBJ_DESC",
        $LOC_OWNER,
        $OBJ_PORTABLE_OR_NOT
    );
    """;
}
