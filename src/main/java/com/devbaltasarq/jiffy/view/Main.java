// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.nio.file.Path;
import java.io.IOException;
import javax.swing.SwingUtilities;

import com.devbaltasarq.jiffy.core.AppInfo;
import com.devbaltasarq.jiffy.core.errors.CompileError;


/** App's entry point.
  * @author baltasarq
  */
public class Main {
    private static void consoleApp(final ConsoleApp APP)
    {
        System.out.println(AppInfo.COMPLETE_NAME );
        System.out.println();

        // Compile
        try {
            System.out.println( "Compiling " + APP.getSourceFile() );
            APP.compile();
            System.out.println( "Ok" );
        } catch(CompileError exc) {
            System.err.println( "[CMPL] " + exc.getMessage() );
        } catch(IOException exc) {
            System.err.println( "[IO] " + exc.getMessage() );
        } catch(Throwable exc) {
            System.err.println( "[ERR] unexpected: " + exc.getMessage() );
        }
    }
    
    private static void guiApp(String path)
    {
        // Prepare look & feel, if possible
        try {
            System.setProperty( "swing.aatext", "true" );
            System.setProperty( "awt.useSystemAAFontSettings", "on" );
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch(Exception ignored) {
        }

        // Launch Gui
        try {
            SwingUtilities.invokeLater( () -> {
                MainWindow mainWin;
                
                if ( path.isEmpty() ) {
                    mainWin = new MainWindow( null );
                } else {
                    mainWin = new MainWindow( Path.of( path ) );
                }
                
                mainWin.getView().setVisible( true );
            });
        } catch(Exception exc)
        {
            JOptionPane.showMessageDialog(
                                null,
                                "Unexpected error: " + exc.getMessage(),
                                AppInfo.NAME,
                                JOptionPane.ERROR_MESSAGE );
        }

        return;
    }
    
    public static void chkFile(File f, final List<String> ARGS)
    {
        if ( f == null ) {
            System.err.println( "[ERR] File not found: '"
                                + String.join( ", ", ARGS ) + "'" );
            System.exit( -2 );
        }
    }
    
    public static void main(String[] args)
    {
        final List<String> PRG_ARGS = new ArrayList<>( Arrays.asList( args ) );
        final ConsoleApp APP = new ConsoleApp( PRG_ARGS );
        var opts = APP.getOptions();
        
        if ( opts.contains( ConsoleApp.Option.COMPILE ) ) {
            chkFile( APP.getSourceFile().get(), PRG_ARGS );
            consoleApp( APP );
        } else {
            String path = "";
            
            if ( args.length > 0 ) {
                path = args[ 0 ];
            }
            
            guiApp( path );
        }
    }
}
