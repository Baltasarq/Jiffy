// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.ast.Loc;
import com.devbaltasarq.jiffy.core.ast.Obj;
import com.devbaltasarq.jiffy.core.ast.Story;
import com.devbaltasarq.jiffy.core.errors.CompileError;


public final class AST {
    public final String IF_VBLES = " " + Id.author() + " "
                                    + " " + Id.start() + " "
                                    + " " + Id.pic() + " "
                                    + " " + Id.version() + " ";

    public AST() throws CompileError
    {
        this.current = this.story = new Story( this, Id.EMPTY_STORY_ID );
    }

    public void add(Entity entity) throws CompileError
    {
        if ( entity instanceof Loc loc ) {
            this.story.add( loc );
            this.current = entity;
        } else {
            throw new Error( "AST.add(): trying to add an entity which is not a loc" );
        }

        return;
    }

    /** @return the story after compiled. */
    public Story getStory()
    {
        return this.story;
    }
    
    /** Allows the story object to be changed by
      * the compiled story, which is presented a Loc.
      * @param LOC the loc that was presented as the story.
      * @throws CompileError if LOC's id is empty or blank.
      */
    public void changeStoryFor(final Loc LOC) throws CompileError
    {
        boolean changeCurrent = ( this.current == this.story );
        
        this.story = new Story(
                            this,
                            LOC.getId().get() );
        
        this.story.changeTitle( LOC.getTitle() );
        this.story.setDesc( LOC.getDesc() );
        
        if ( changeCurrent ) {
            this.current = this.story;
        }
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

    /** @return a loc, given its id, or null if not found.
      * @param id the given id.
      */
    public Loc findLocByStrId(String id)
    {
        try {
            return this.getStory().getLoc( new Id( id ) );
        } catch(CompileError exc) {
            return null;
        }
    }
    
    /** @return a loc, given its id, or null if not found.
      * @param id the given id.
      */
    public Loc findLocById(Id id)
    {
        return this.getStory().getLoc( id );
    }

    /** @return an obj, given its, or null if not found.
      *         It always returns the first found, while
      *         running over all locs.
      * @param id a given id.
      */
    public Obj findObjByStrId(String id)
    {
        try {
            return this.findObjById( new Id( id ) );
        } catch(CompileError exc) {
            return null;
        }
    }
    
    /** @return an obj, given its, or null if not found.
      *         It always returns the first found, while
      *         running over all locs.
      * @param id a given id.
      */
    public Obj findObjById(Id id)
    {
        Obj toret = null;

        for(final Loc LOC: this.getStory().getLocs()) {
            toret = LOC.getObj( id );
            
            if ( toret != null ) {
                break;
            }
        }

        return toret;
    }

    private Entity current;
    private Story story;
}
