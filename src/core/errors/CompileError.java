// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.errors;


public class CompileError extends Exception {
    public CompileError(int ln, String msg)
    {
        this(ln + ": " + msg );
    }

    public CompileError(int ln, int coln, String msg)
    {
        this(ln + ", " + coln + ": " + msg );
    }

    public CompileError(String msg)
    {
        super( "[ERR] " + msg );
    }
}
