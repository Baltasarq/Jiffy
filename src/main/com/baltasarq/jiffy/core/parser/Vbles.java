// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core.parser;

import com.baltasarq.jiffy.core.Id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Vbles {
    public Vbles()
    {
        this.VBLES = new HashMap<>();
    }

    public Var getById(Id id)
    {
        return this.VBLES.get( id );
    }

    public Var getAt(int i)
    {
        return null;//this.VBLES.values().get
    }

    public int size()
    {
        return this.VBLES.size();
    }

    public List<Var> getAll()
    {
        return new ArrayList<Var>( this.VBLES.values() );
    }

    public void add(Var vble)
    {
        if ( vble == null ) {
            throw new Error( "AST.add(): trying to insert a null OBJ" );
        }

        this.VBLES.put( vble.getId(), vble );
    }

    private final Map<Id, Var> VBLES;
}
