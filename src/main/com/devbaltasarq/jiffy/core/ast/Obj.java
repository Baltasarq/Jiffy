// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.errors.CompileError;


// Represents objects (inside locs).
public class Obj extends Entity {
    public Obj(final AST AST, String name, final Loc OWNER) throws CompileError
    {
        super( AST, name );
        this.OWNER = OWNER;
    }

    /** @return the id of the owner loc. */
    public Loc getOwner()
    {
        return this.OWNER;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " /" + super.toString();
    }

    private final Loc OWNER;
}
