// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.IndexedCatalog;
import com.devbaltasarq.jiffy.core.errors.CompileError;

import java.util.List;
import java.util.Map;
import java.util.HashMap;


/** Represents locs (rooms). Rooms can have objects inside. */
public class Loc extends Entity {
    public Loc(final AST AST, String name) throws CompileError
    {
        super( AST, name );
        this.current = null;
        this.exits = new HashMap<>();
        this.OBJS = new IndexedCatalog();
    }
    
    /** @return an Id given a direction, if an exit exists,
      *           or null otherwise.
      * @param dir the given direction.
      */
    public Id getExitAt(Direction dir)
    {
        if ( dir == null ) {
            throw new Error( "getExitAt(): direction can't be null" );
        }
        
        return this.exits.get( dir );
    }
    
    /** @return a list of all directions that have an exit. */
    public List<Direction> getAllExitDirections()
    {
        return this.exits.keySet().stream().toList();
    }
    
    /** Sets a new exit to a given loc (through an id) at a given direction.
      * @param dir the given direction.
      * @param id a given id.
      */
    public void setExitAt(Direction dir, Id id)
    {
        if ( dir == null ) {
            throw new Error( "setExitAt(): direction can't be null" );
        }
        
        if ( id == null ) {
            throw new Error( "setExitAt(): loc can't be null" );
        }
        
        this.exits.put( dir, id );
    }

    /** Stores a new Obj.
      * @param obj an Obj for this Loc.
      * @throws CompileError if the id already exists.
      */
    public void add(Obj obj) throws CompileError
    {
        if ( obj == null ) {
            throw new Error( "Loc.add(): trying to insert a null OBJ" );
        }

        final Id ID = obj.getId();

        if ( this.OBJS.get( ID ) != null ) {
            throw new CompileError( "object '"
                    + ID
                    + "' already exists in: " + this.getId() );
        }

        this.OBJS.add( obj );
        this.current = obj;
    }

    /** @return the object for the given id.
      * @param id the given id.
      */
    public Obj getObj(Id id)
    {
        return this.OBJS.get( id );
    }

    /** @return the current object being compiled. */
    public Obj current()
    {
        return this.current;
    }

    /** @return all the objects for this room. */
    public List<Obj> getObjs()
    {
        return this.OBJS.all();
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " /" + super.toString();
    }

    private Obj current;
    private final Map<Direction, Id> exits;
    private final IndexedCatalog<Obj> OBJS;
}
