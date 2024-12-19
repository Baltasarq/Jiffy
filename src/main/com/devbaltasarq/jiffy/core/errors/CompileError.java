// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.errors;


public class CompileError extends Exception {
    public CompileError(int ln, String msg)
    {
        this(ln + ": " + msg );
    }

    public CompileError(int ln, int coln, String msg)
    {
        this(ln + ", " + coln + ": [ERR] " + msg );
    }

    public CompileError(String msg)
    {
        super( msg );
    }
}
