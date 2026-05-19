// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.errors;


public class EmitError extends Exception {
    public EmitError(String entity, String msg)
    {
        this( entity + ": " + msg );
    }

    public EmitError(String msg)
    {
        super( msg );
    }
}
