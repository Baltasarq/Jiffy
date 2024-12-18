// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core.emitter;


import com.baltasarq.jiffy.core.AST;
import com.baltasarq.jiffy.core.Emitter;
import com.baltasarq.jiffy.core.ast.Loc;
import com.baltasarq.jiffy.core.ast.Obj;
import com.baltasarq.jiffy.core.emitter.templates.fijs.StoryCommentHeader;

import java.io.IOException;
import java.io.Writer;


/** Emits code for the fi.js framework. */
public class FiJsEmitter extends Emitter {
    public FiJsEmitter(final AST AST)
    {
        super( AST );
    }

    /** Does the real work of emitting fi-js code. */
    @Override
    public void emit(Writer out) throws IOException
    {
        final AST AST = this.getAst();

        // Story info in comments
        out.write( new StoryCommentHeader( AST.getStory() ).subst() );

        // All locs
        for(Loc loc: this.getAst().getLocs() ) {
            out.write( new com.baltasarq.jiffy.core.emitter
                                            .templates.fijs.Loc( loc ).subst() );

            // All objs inside the loc
            for(Obj obj: loc.getObjs()) {
                out.write( new com.baltasarq.jiffy.core.emitter
                                            .templates.fijs.Obj( obj ).subst() );
            }
        }

        // The story info
        out.write( new com.baltasarq.jiffy.core.emitter
                                    .templates.fijs.StoryInfo( AST ).subst() );
    }
}
