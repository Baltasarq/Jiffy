// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.emitter;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Emitter;
import com.devbaltasarq.jiffy.core.ast.Loc;
import com.devbaltasarq.jiffy.core.emitter.templates.fijs.StoryCommentHeader;
import com.devbaltasarq.jiffy.core.errors.EmitError;

import java.io.IOException;
import java.io.Writer;


/** Emits code for the fi.js framework. */
public class FiJsEmitter extends Emitter {
    public FiJsEmitter(final AST AST)
    {
        super( AST );
    }

    /** Does the real work of emitting fi-js code.
      * @param out where to write the story.
      * @throws IOException if out cannot be written.
      */
    @Override
    public void emit(Writer out) throws IOException, EmitError
    {
        final AST AST = this.getAst();

        // Story info in comments
        out.write( new StoryCommentHeader( AST.getStory() ).subst() );

        // All locs
        for(Loc loc: this.getAst().getStory().getLocs() ) {
            out.write( new com.devbaltasarq.jiffy.core.emitter
                                            .templates.fijs.Loc( loc ).subst() );
        }

        // The story info
        out.write( new com.devbaltasarq.jiffy.core.emitter
                                    .templates.fijs.StoryInfo( AST ).subst() );
    }
}
