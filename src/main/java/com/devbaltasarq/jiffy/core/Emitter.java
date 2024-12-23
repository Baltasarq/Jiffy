// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import java.io.*;
import java.util.Map;


/** Represents all possible code emitters. */
public abstract class Emitter {
    /** Creates a new emitter.
      * @param AST the ast data to work with.
      */
    protected Emitter(final AST AST)
    {
        this.ast = AST;
    }

    /** A convenience method to output to a set of files, with the
      * same name but different extensions.
      * @param fileNameNoExt the file name (without extension) to output
      *                      the various files to.
      * @see Emitter::emit
      */
    public void emit(String fileNameNoExt) throws IOException
    {
        final String MAIN_CODE = fileNameNoExt + ".js";

        try (final PrintWriter WRT = new PrintWriter( MAIN_CODE )) {
            this.emit( WRT );
        }
    }

    /** This method will do the magic, emitting to a given outputstream.
      * @param f the OutputStream to write to.
      */
    public abstract void emit(Writer f) throws IOException;

    /** @return the AST this object is running. */
    public AST getAst()
    {
        return this.ast;
    }

    /** Generates final text from a template and substitutions.
      * i.e. "this is a $OBJ_NAME" and $OBJ_NAME -> "dog", then
      * the outcome is "this is a dog"
      * @param TEMPLATE the template to apply the substitutions to.
      * @param SUBST the substitutions to apply to the template.
      * @return the final text.
      */
    public String textFromTemplate(final String TEMPLATE,
                                   final Map<String, String> SUBST)
    {
        String toret = TEMPLATE;

        for(final Map.Entry<String, String> PAIR: SUBST.entrySet()) {
            toret = toret.replace( PAIR.getKey(), PAIR.getValue() );
        }

        return toret;
    }

    private final AST ast;
}
