// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.ast;


import core.AST;
import core.Id;
import core.errors.CompileError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Represents locs (rooms). Rooms can have objects inside. */
public class Loc extends Entity {
    public Loc(final AST AST, String name) throws CompileError
    {
        super( AST, name );
        this.current = null;
        this.OBJS = new HashMap<>();

        // Put the first char in upper case
        this.title = name.trim().toLowerCase();
        this.title = this.title.substring( 0, 1 ).toUpperCase()
                     + this.title.substring( 1 );
    }

    /** @return the title of this room. */
    public String getTitle()
    {
        return this.title;
    }

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

        this.OBJS.put( obj.getId(), obj );
        this.current = obj;
    }

    public Obj getObj(Id id)
    {
        return this.OBJS.get( id );
    }

    public Obj current()
    {
        return this.current;
    }

    public List<Obj> getObjs()
    {
        return new ArrayList<>( this.OBJS.values() );
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " /" + super.toString();
    }

    private Obj current;
    private String title;
    private final Map<Id, Obj> OBJS;
}
