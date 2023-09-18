// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core;


import core.ast.Entity;
import core.ast.Loc;
import core.ast.Story;
import core.errors.CompileError;

import java.util.List;
import java.util.ArrayList;


public final class AST {
    public final String IF_VBLES = " author ";

    public AST() throws CompileError
    {
        this.current = this.STORY = new Story( Id.empty() );
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

    public List<Loc> getLocs()
    {
        return new ArrayList<>( this.STORY.getLocs() );
    }

    private Entity current;
    private final Story STORY;
}
