// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package view;


import java.io.*;
import java.util.*;

import core.AST;
import core.Util;
import core.AppInfo;
import core.Emitter;
import core.Parser;
import core.ast.JsonWriter;
import core.emitter.FiJsEmitter;
import core.emitter.templates.fijs.Css;
import core.emitter.templates.fijs.Html;
import core.errors.CompileError;


public class Main {
    private enum Option { ERROR, HELP, JSON;
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

    private static Set<Option> extractOptions(List<String> args)
    {
        final Set<Option> TORET = new HashSet<>( args.size() - 1 );

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

    private static void compileToFiJs(final AST AST,
                                      final String TARGET_FILE)
            throws IOException
    {
        // Generate target file (*.js)
        final Emitter EMITTER = new FiJsEmitter( AST );
        EMITTER.emit( TARGET_FILE );

        // Generate HTML companion file
        final Html HTML_TEMPLATE = new Html( TARGET_FILE, AST.getStory() );
        try (final PrintWriter WR = new PrintWriter( TARGET_FILE + ".html" ))
        {
            WR.write( HTML_TEMPLATE.subst() );
        }

        // Generate CSS companion file
        final Css CSS_TEMPLATE = new Css( AST.getStory() );
        try (final PrintWriter WR = new PrintWriter( TARGET_FILE + ".css" ))
        {
            WR.write( CSS_TEMPLATE.subst() );
        }
    }

    public static void main(String[] args)
    {
        final List<String> PRG_ARGS = new ArrayList<>( Arrays.asList( args ) );
        System.out.println( AppInfo.CompleteName );
        System.out.println();

        // Parse arguments
        final Set<Option> OPTS = extractOptions( PRG_ARGS );

        if ( OPTS.contains( Option.ERROR ) ) {
            System.err.println( "[ERR] Parsing arguments: "
                                + PRG_ARGS.get( 0 ) );
            System.exit( -1 );
        }

        // Determine source file
        if ( PRG_ARGS.isEmpty() ) {
            System.err.println( "[ERR] Expected file name." );
            System.exit( -1 );
        }

        final File SOURCE_FILE = new File( PRG_ARGS.get( 0 ) );

        if ( !SOURCE_FILE.exists() ) {
            System.err.println( "[ERR] File not found: " + SOURCE_FILE );
            System.exit( -2 );
        }

        // Compile
        System.out.println( "Compiling " + SOURCE_FILE );
        final String TARGET_FILE = Util.removeExt( SOURCE_FILE.getAbsolutePath() );

        try {
            final Parser PARSE = new Parser();
            final AST AST = PARSE.parseFile( SOURCE_FILE.getAbsolutePath() );

            // Generate json?
            if ( OPTS.contains( Option.JSON ) ) {
                // Generate JSON
                new JsonWriter( AST ).write( new File( TARGET_FILE + ".json" ) );
            } else {
                compileToFiJs( AST, TARGET_FILE );
            }

            System.out.println( "Ok" );
        } catch(CompileError exc) {
            System.err.println( "[CMPL] " + exc.getMessage() );
        } catch(IOException exc) {
            System.err.println( "[IO] " + exc.getMessage() );
        }
    }
}
