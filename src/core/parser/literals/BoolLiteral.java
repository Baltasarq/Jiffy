// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.parser.literals;

import core.parser.Literal;


/** Represents boolean literals. */
public class BoolLiteral extends Literal {
    public BoolLiteral(boolean lit)
    {
        super( lit );
    }

    public Boolean get()
    {
        return (Boolean) super.get();
    }

    public void setValue(boolean x)
    {
        super.set( (Boolean) x );
    }

    @Override
    public String toString() {
        return Boolean.toString( this.get() );
    }
}
