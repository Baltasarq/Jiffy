// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core;


import com.baltasarq.jiffy.core.ast.Entity;
import com.baltasarq.jiffy.core.ast.Loc;
import com.baltasarq.jiffy.core.ast.Obj;
import com.baltasarq.jiffy.core.ast.Story;
import com.baltasarq.jiffy.core.errors.CompileError;

import java.util.List;
import java.util.ArrayList;


public final class AST {
    public final String IF_VBLES = " author intro start pic version ";

    public AST() throws CompileError
    {
        this.current = this.STORY = new Story( this, Id.EMPTY_STORY_ID );
    }

    public void add(Entity entity) throws CompileError
    {
        if ( entity instanceof Loc loc ) {
            this.STORY.add( loc );
            this.current = entity;
        } else {
            throw new Error( "AST.add(): trying to add an entity which is not a loc" );
        }

        return;
    }

    public Story getStory()
    {
        return this.STORY;
    }

    public Entity current(Parser.Status st)
    {
        Entity toret = this.getStory();

        if ( st == Parser.Status.LOC ) {
            toret = this.current;
        }
        else
        if ( st == Parser.Status.OBJ ) {
            toret = null;

            if ( this.current instanceof Loc loc ) {
                toret = loc.current();
            }
        }

        return toret;
    }

    public Loc findLocById(String id)
    {
        Loc toret = null;

        for(final Loc LOC: this.getLocs()) {
            if ( LOC.getId().get().equals( id ) ) {
                toret = LOC;
                break;
            }
        }

        return toret;
    }

    public Obj findObjById(String id)
    {
        Obj toret = null;

        for(final Loc LOC: this.getLocs()) {
            for(final Obj OBJ: LOC.getObjs()) {
                if ( OBJ.getId().get().equals( id ) ) {
                    toret = OBJ;
                    break;
                }
            }
        }

        return toret;
    }

    public List<Loc> getLocs()
    {
        return new ArrayList<>( this.STORY.getLocs() );
    }

    private Entity current;
    private final Story STORY;
}
