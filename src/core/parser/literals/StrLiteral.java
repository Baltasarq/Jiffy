// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.parser.literals;

import core.parser.Literal;


/** Represents string literals. */
public class StrLiteral extends Literal {

    public StrLiteral(String lit)
    {
        super( lit );
    }

    public String get()
    {
        return (String) super.get();
    }

    @Override
    public String toString() {
        return (String) this.get();
    }
}
