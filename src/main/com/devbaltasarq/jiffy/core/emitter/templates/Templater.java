// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter.templates;


import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.parser.RValue;
import com.devbaltasarq.jiffy.core.parser.Var;

import java.util.Map;


public abstract class Templater {
    public Templater(final Entity ENT)
    {
        this.ENT = ENT;
    }

    /** @return the related entity. */
    public Entity getEntity()
    {
        return this.ENT;
    }

    /** Given an Entity, it makes the substituions in the template. */
    public abstract String subst();

    /** Applies all the given substitutions to the given template.
      * @param TEMPLATE the string with the placeholders.
      * @param SUBSTS the dictionary with the substitutions.
      * @return a string with all the substitutions applied.
      */
    protected String applySubsts(final String TEMPLATE,
                                 final Map<String, String> SUBSTS)
    {
        String toret = TEMPLATE;

        for(Map.Entry<String, String> entry: SUBSTS.entrySet()) {
            toret = toret.replace( entry.getKey(), entry.getValue() );
        }

        return toret;
    }

    /** Merges two maps of substitutions.
      * Puts the entry pairs of DEFAULT_SUBSTS in SUBSTS, provided
      * that they are not already present; in that case nothing is done.
      * @param DEFAULT_SUBSTS the map with the default substitutions.
      * @param SUBSTS the map with the actual substitutions.
      */
    protected void mergeSubstMaps(final Map<String, String> DEFAULT_SUBSTS,
                                  final Map<String, String> SUBSTS)
    {
        DEFAULT_SUBSTS.forEach( ( key, value ) -> {
            if ( !SUBSTS.containsKey( key ) ) {
                SUBSTS.put( key, value );
            }
        });

        return;
    }

    protected RValue getVar(String id)
    {
        RValue toret = null;

        try {
            final Var VAR = this.ENT.getVbles().getById( new Id( id ) );

            if ( VAR != null ) {
                toret = VAR.getRValue();
            }
        } catch(CompileError exc) {
            toret = null;
        }

        return toret;
    }

    private final Entity ENT;
}
