// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.ast;


import core.Id;
import core.errors.CompileError;
import core.parser.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Entity {
    public Entity(Id id)
    {
        this.id = id;
        this.desc = "";
        this.VBLES = new HashMap<>();
    }

    public Id getId()
    {
        return this.id;
    }

    public void setDesc(String desc)
    {
        this.desc = HTMLfromMD( desc );
    }

    public String getDesc()
    {
        return desc;
    }

    public void add(Var vble) throws CompileError
    {
        if ( vble == null ) {
            throw new Error( "AST.add(): trying to insert a null OBJ" );
        }

        final Id ID = vble.getId();

        if ( this.VBLES.get( ID ) != null ) {
            throw new CompileError( "variable '"
                                    + ID
                                    + "' already exists in: " + this.id);
        }

        this.VBLES.put( vble.getId(), vble );
    }

    public Var getVble(Id id)
    {
        return this.VBLES.get( id );
    }

    public List<Var> getVbles()
    {
        return new ArrayList<>( this.VBLES.values() );
    }

    private String HTMLfromMD(String txt)
    {
        txt = txt.trim();

        return txt;
    }

    @Override
    public String toString()
    {
        return this.id.get();
    }

    private String desc;
    protected Id id;
    private final Map<Id, Var> VBLES;
}
