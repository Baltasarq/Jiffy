// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.parser;


/** Literals base class. */
public abstract class Literal extends RValue {

    public Literal(Object value)
    {
        this.set( value );
    }

    public Object get()
    {
        return this.value;
    }

    public void set(Object value)
    {
        this.value = value;
    }

    public abstract String toString();

    private Object value;
}
