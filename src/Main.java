// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


import java.io.File;


public class Main {
    public static void main(String[] args)
    {
        System.out.println( AppInfo.CompleteName );
        System.out.println();

        if ( args.length < 1 ) {
            System.err.println( "[ERR] Expected file name." );
            System.exit( -1 );
        }

        File source = new File( args[ 0 ] );

        if ( !source.exists() ) {
            System.err.println( "[ERR] File not found: " + source );
            System.exit( -2 );
        }

        System.out.println( "Compiling " + source );
    }
}
