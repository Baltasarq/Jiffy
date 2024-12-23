// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser.literals;


import com.devbaltasarq.jiffy.core.parser.Literal;


/** Represents floating point literals. */
public class RealLiteral extends Literal {
    public RealLiteral(double lit)
    {
        super( lit );
    }

    public Double get()
    {
        return (Double) super.get();
    }

    public void setValue(double x)
    {
        super.set( (Double) x );
    }

    @Override
    public String toString() {
        return Double.toString( this.get() );
    }
}
