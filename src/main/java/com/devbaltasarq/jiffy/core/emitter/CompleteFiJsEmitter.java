/*
 * // YYY (c) 2xxx Baltasar MIT License <baltasarq@gmail.com>
 */
package com.devbaltasarq.jiffy.core.emitter;

import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Emitter;
import com.devbaltasarq.jiffy.core.emitter.templates.fijs.Css;
import com.devbaltasarq.jiffy.core.emitter.templates.fijs.Html;
import com.devbaltasarq.jiffy.core.errors.EmitError;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author baltasarq
 */
public class CompleteFiJsEmitter {
    public CompleteFiJsEmitter(final AST AST, final String TARGET_FILE)
    {
        this.ast = AST;
        this.targetFile = TARGET_FILE;
    }
    
    public void emit() throws IOException, EmitError
    {
        // Generate target file (*.js)
        final Emitter EMITTER = new FiJsEmitter( this.ast );
        EMITTER.emit( this.targetFile );

        // Generate HTML companion file
        final Html HTML_TEMPLATE = new Html( this.targetFile,
                                            this.ast.getStory() );
        try (final PrintWriter WR = new PrintWriter( this.targetFile + ".html" ))
        {
            WR.write( HTML_TEMPLATE.subst() );
        }

        // Generate CSS companion file
        final Css CSS_TEMPLATE = new Css( this.ast.getStory() );
        try (final PrintWriter WR = new PrintWriter( this.targetFile + ".css" ))
        {
            WR.write( CSS_TEMPLATE.subst() );
        }
    }
    
    private final AST ast;
    private final String targetFile;
}
