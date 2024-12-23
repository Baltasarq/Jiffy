// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser.literals;


import com.devbaltasarq.jiffy.core.parser.Literal;


/** Represents number (integer) literals. */
public class IntLiteral extends Literal {
    public IntLiteral(long lit)
    {
        super( lit );
    }

    public Long get()
    {
        return (Long) super.get();
    }

    public void setValue(long x)
    {
        super.set( (Long) x );
    }

    @Override
    public String toString() {
        return Long.toString( this.get() );
    }
}
