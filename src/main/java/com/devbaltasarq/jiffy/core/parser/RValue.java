// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.parser;


/** Represents all evaluable parts inside a method. */
public abstract class RValue {
    /** @return the value itself. */
    public abstract Object get();

    @Override
    public String toString()
    {
        return this.get().toString();
    }
}
