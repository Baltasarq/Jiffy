// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser;


import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.Identifiable;


public class Var implements Identifiable {
    public Var(Id id, RValue rvalue)
    {
        this.ID = id;
        this.RVALUE = rvalue;
    }

    public RValue getRValue()
    {
        return this.RVALUE;
    }

    @Override
    public Id getId()
    {
        return this.ID;
    }

    @Override
    public String toString()
    {
        return this.getId().get() + " = " + this.getRValue().toString();
    }

    private final Id ID;
    private final RValue RVALUE;
}
