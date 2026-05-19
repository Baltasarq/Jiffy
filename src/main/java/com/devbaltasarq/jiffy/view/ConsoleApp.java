// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Emitter;
import com.devbaltasarq.jiffy.core.Parser;
import com.devbaltasarq.jiffy.core.SourceFile;
import com.devbaltasarq.jiffy.core.ast.JsonWriter;
import com.devbaltasarq.jiffy.core.emitter.CompleteFiJsEmitter;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.emitter.FiJsEmitter;
import com.devbaltasarq.jiffy.core.emitter.templates.fijs.Css;
import com.devbaltasarq.jiffy.core.emitter.templates.fijs.Html;
import com.devbaltasarq.jiffy.core.errors.EmitError;
import java.io.File;


public class ConsoleApp {
    public enum Option { ERROR, HELP, JSON, COMPILE;
        public static Option parse(String strOpt)
        {
            Option toret;

            // Remove beginning "--", if needed.
            if ( strOpt.startsWith( "--" ) ) {
                strOpt = strOpt.substring( 2 );
            }

            // Convert to option
            strOpt = strOpt.toUpperCase();

            try {
                toret = valueOf( strOpt );
            } catch(IllegalArgumentException exc) {
                toret = ERROR;
            }

            return toret;
        }
    }
    
    /** Configures a new console app, given the args.
      * @param ARGS the arguments passed to the entry point.
      */
    public ConsoleApp(final List<String> ARGS)
    {
        this.opts = this.extractOptions( ARGS );
        this.source = null;
        
        // Find the source file
        if ( !ARGS.isEmpty() ) {
            this.source = new SourceFile( ARGS.get( 0 ) );
        }
        
        if ( this.source != null
          && !this.source.get().exists() )
        {
            this.source = null;
        }
    }
    
    /** @return the options parsed, or an empty set if error. */
    public Set<Option> getOptions()
    {
        final Set<Option> TORET = new HashSet<>();
    
        if ( !this.opts.contains( Option.ERROR ) ) {
            TORET.addAll( this.opts );
        }
        
        return TORET;
    }
    
    /** @return the source file, or null if it does not exist. */
    public SourceFile getSourceFile()
    {
        return this.source;
    }
    
    /** @return the target file, or null if the source file does not exist. */
    
    
    /** Compile and generate, with the provided options.
      * @throws CompileError when there is a compile error.
      * @throws EmitError when there is an emission error.
      * @throws IOException  when a file is not found.
      */
    public void compile() throws CompileError, EmitError, IOException
    {
        final Parser PARSE = new Parser();
        final var TARGET_FILE = this.source.buildTarget();
        
        if ( this.source != null ) {
            final AST AST = PARSE.parseFile( this.source.get().getAbsolutePath() );

            // Generate json?
            if ( this.opts.contains( Option.JSON ) ) {
                final File TARGET_JSON = new File(
                                                TARGET_FILE.getAbsoluteFile()
                                                + ".json" );
                // Generate JSON
                new JsonWriter( AST ).write( TARGET_JSON );
            } else {
                new CompleteFiJsEmitter( AST, TARGET_FILE.getAbsolutePath() ).emit();
            }
        } else {
            throw new IOException( "source file not found: " + this.source );
        }
    }
    
    private Set<Option> extractOptions(List<String> args)
    {
        final int CAPACITY = Math.max( 1, args.size() );
        final Set<Option> TORET = new HashSet<>( CAPACITY );

        while ( !args.isEmpty() ) {
            String arg = args.get( 0 );

            if ( arg.startsWith( "--" ) ) {
                final Option OPT = Option.parse( arg );

                TORET.add( OPT );

                if ( OPT == Option.ERROR ) {
                    break;
                }

                args.remove( 0 );
            } else {
                // We exit the loop so only the target file name remains.
                break;
            }
        }

        return TORET;
    }

    private SourceFile source;
    private final Set<Option> opts;
}
