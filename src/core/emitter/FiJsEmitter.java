// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.emitter;


import core.AST;
import core.Emitter;
import core.ast.Loc;
import core.ast.Obj;

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
        out.write( new core.emitter.templates.fijs.StoryCommentHeader(
                                                AST.getStory() ).subst() );

        // All locs
        for(Loc loc: this.getAst().getLocs() ) {
            out.write( new core.emitter.templates.fijs.Loc( loc ).subst() );

            // All objs inside the loc
            for(Obj obj: loc.getObjs()) {
                out.write( new core.emitter.templates.fijs.Obj( obj ).subst() );
            }
        }

        // The story info
        out.write( new core.emitter.templates.fijs.StoryInfo(
                                            AST.getStory(),
                                            AST.getLocs().get( 0 ) ).subst() );
    }
}
