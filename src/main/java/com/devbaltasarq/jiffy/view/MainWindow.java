// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.net.URI;
import java.awt.Desktop;
import java.nio.file.Path;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.AppInfo;
import com.devbaltasarq.jiffy.core.Parser;
import com.devbaltasarq.jiffy.core.SourceFile;
import com.devbaltasarq.jiffy.core.ast.JsonWriter;
import com.devbaltasarq.jiffy.core.ast.TrizbortWriter;
import com.devbaltasarq.jiffy.core.emitter.CompleteFiJsEmitter;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.errors.EmitError;
import com.devbaltasarq.jiffy.core.parser.Templates;
import java.io.File;


/** The main window controller.
  * @author baltasarq
  */
public final class MainWindow {
    public MainWindow(Path path)
    {
        this.view = new MainWindowView();
        this.editor = new Editor( this.view.getEditorView() );
        this.view.setNewAction( () -> this.doNew() );
        this.view.setLoadAction( () -> this.doLoad() );
        this.view.setSaveAction( () -> this.doSave() );
        this.view.setQuitAction( () -> this.doQuit() );
        this.view.setCompileAction( () -> this.doCompile() );
        this.view.setRunAction( () -> this.doRun() );
        this.view.setInsertLocAction( () -> this.doInsertLoc() );
        this.view.setInsertObjAction( () -> this.doInsertObj() );
        this.view.setHelpAction( () -> this.doHelp() );
        this.view.setAboutAction( () -> this.doAbout() );
        this.view.setExportToJsonAction( () -> this.doExportJson() );
        this.view.setExportToTrizbortAction( () -> this.doExportTrizbort() );
        
        this.startFromScratch();
        
        if ( path != null ) {
            String errorMsg = "";
            
            if ( path.toFile().exists() ) {
                try {
                    this.getEditor().loadFromPath( path );
                } catch(IOException exc) {
                    errorMsg = exc.getMessage();
                }
            } else {
                errorMsg = "file does not exist: " + path;
            }
            
            if ( !errorMsg.isEmpty() ) {
                this.getView().setVisible( true );
                JOptionPane.showMessageDialog(
                            this.getView(),
                            "Error loading document: " + errorMsg,
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
            }
        }
    }
    
    /** @return the main window view. */
    public MainWindowView getView()
    {
        return this.view;
    }
    
    /** @return the editor. */
    public Editor getEditor()
    {
        return this.editor;
    }
    
    private void doNew()
    {
        // First save the current source
        this.doSave();
        
        // Restart
        this.clearOutput();
        this.show(  "Starting from scratch." );
        this.startFromScratch();        
    }
    
    /** Loads a document into the editor. */
    private void doLoad()
    {
        // First save the current source
        this.doSave();
        
        // Now ask for a new source to load
        final var FILE_DLG = new JFileChooser();
        
        this.configure( FILE_DLG );
        int dlgResult = FILE_DLG.showOpenDialog( this.getView() );
        
        if ( dlgResult == JFileChooser.APPROVE_OPTION ) {
            try {
                this.getEditor().loadFromPath( FILE_DLG.getSelectedFile().toPath() );
            } catch(IOException exc)
            {
                JOptionPane.showMessageDialog(
                        this.getView(),
                        "Error loading document: " + exc.getMessage(),
                        AppInfo.NAME,
                        JOptionPane.ERROR_MESSAGE );
            }
        }
        
        return;
    }
    
    private void configure(final JFileChooser FILE_CHOOSER)
    {
        Path currentDir = this.getEditor().getPath();
        
        if ( currentDir == null ) {
            currentDir = Path.of( System.getProperty( "user.home", "." ) );
        }
        
        FILE_CHOOSER.setCurrentDirectory( currentDir.toFile() );
        FILE_CHOOSER.setFileFilter(
                        new FileNameExtensionFilter(
                                        "Jiffy files (*.jiffy)",
                                        "jiffy" ) );
        FILE_CHOOSER.setFileFilter(
                        new FileNameExtensionFilter(
                                        "Text files (*.txt)",
                                        "txt" ) );
    }
    
    private Path askPath()
    {
        final var FILE_CHOOSER = new JFileChooser();
        Path toret = null;
        int result = FILE_CHOOSER.showSaveDialog( this.getView() );
        
        if ( result == JFileChooser.APPROVE_OPTION ) {
            toret = FILE_CHOOSER.getSelectedFile().toPath();
        }
        
        return toret;
    }
    
    /** Saves the document from the editor. */
    private void doSave()
    {
        if ( this.editor.getPath() == null ) {
            this.editor.assignPath( this.askPath() );
        }
        
        if ( this.editor.getPath() != null ) {
            try {
                this.editor.save();
            } catch(IOException exc) {
                JOptionPane.showMessageDialog(
                        this.getView(),
                        "Error saving document: " + exc.getMessage(),
                        AppInfo.NAME,
                        JOptionPane.ERROR_MESSAGE );
            }
        }
        
        return;
    }
    
    /** Finish the app. */
    private void doQuit()
    {
        this.getView().setVisible( false );
        System.exit( 0 );
    }
    
    /** Compile the source code. */
    private void doCompile()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No input file",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
        
        this.doSave();
        
        try {
            final Parser PARSE = new Parser();
            
            this.clearOutput();
            this.show( "Compiling: " + path );
            
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "Emitting: " + TARGET_FILE );
            new CompleteFiJsEmitter( AST, TARGET_FILE.getAbsolutePath() ).emit();
        } catch(CompileError | EmitError | IOException exc) {
            this.show( "[CMP] " + exc.getMessage() );
        }
    }
    
    /** Execute the source code. */
    private void doRun()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No input file",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        this.doSave();
        this.doCompile();
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = new File( SOURCE.buildTarget().
                                                getAbsolutePath() + ".html" );

        try {
            this.clearOutput();
            this.show( "Running: " + TARGET_FILE );
                        
            if ( !Desktop.isDesktopSupported() ) {
                throw new UnsupportedOperationException( "no desktop!" );
            }
            
            final Desktop DESK = Desktop.getDesktop();
            
            if ( !DESK.isSupported( Desktop.Action.BROWSE ) ) {
                this.show( "[RUN] No browsing supported, "
                            + "trying to open the directory" );
                DESK.browseFileDirectory(path);
                throw new UnsupportedOperationException( "no browser!" );
            }
            
            DESK.browse( TARGET_FILE.toURI() );
        } catch(UnsupportedOperationException exc) {
            this.show( "[RUN] Unsupported action: " + exc.getMessage() );
        } catch(IOException exc) {
            this.show( "[RUN] I/O Error: " + exc.getMessage() );
        } catch(Exception exc) {
            this.show( "[RUN] Unexpected error: " + exc.getMessage() );
        }
    }
    
    /** Inserts a new loc. */
    private void doInsertLoc()
    {
        
    }
    
    /** Inserts a new obj. */
    private void doInsertObj()
    {
        
    }
    
    /** Inserts any template. */
    private void insertTemplate(String template)
    {
        final var EDITOR = this.view.getEditorView().getEditor();
                
        EDITOR.insert( template , EDITOR.getSelectionStart() );
    }
    
    private void clearOutput()
    {
        this.getView().getOutput().setText( "" );
    }
    
    /** Shows a message through the output panel.
      * @param msg the message to show.
      */
    private void show(String msg)
    {
        this.getView().getOutput().append( msg + "\n" );
    }
    
    /** When the user presses F1. */
    private void doHelp()
    {
        try {
            if ( !Desktop.isDesktopSupported() ) {
                throw new IOException( "desktop not supported" );
            }
            
            final var DESKTOP = Desktop.getDesktop();
            
            DESKTOP.browse( new URI( AppInfo.WIKI_WEB_SITE ) );
        } catch(URISyntaxException | IOException exc)
        {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "Error opening help: " + exc.getMessage(),
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
    }
    
    /** Show app's info. */
    private void doAbout()
    {
        JOptionPane.showMessageDialog(
                        this.getView(),
                        AppInfo.COMPLETE_NAME,
                        AppInfo.NAME,
                        JOptionPane.PLAIN_MESSAGE );
    }
    
    /** Export the current story to JSON. */
    private void doExportJson()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No input file",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
        
        this.doSave();
        
        try {
            final Parser PARSE = new Parser();
            
            this.show( "Compiling: " + path );
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "Saving JSON: " + SOURCE.buildTarget() + ".json" );
            final File TARGET_JSON = new File( TARGET_FILE.getAbsoluteFile() + ".json" );
            new JsonWriter( AST ).write( TARGET_JSON );
        } catch(Exception exc) {
            this.show( "[ERR] " + SOURCE.buildTarget() + ".json: " + exc.getMessage() );
            JOptionPane.showMessageDialog(
                            this.getView(),
                            exc.getMessage(),
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
    }
    
    /** Export the current story to Trizbort. */
    private void doExportTrizbort()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No input file",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
        
        this.doSave();
        
        try {
            final Parser PARSE = new Parser();
            
            this.show( "Compiling: " + path );
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "Saving Trizbort: " + SOURCE.buildTarget() + ".json" );
            final File TARGET_JSON = new File( TARGET_FILE.getAbsoluteFile() + ".json" );
            new TrizbortWriter( AST ).write( TARGET_JSON );
        } catch(Exception exc) {
            this.show( "[ERR] " + SOURCE.buildTarget() + ".json: " + exc.getMessage() );
            JOptionPane.showMessageDialog(
                            this.getView(),
                            exc.getMessage(),
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
    }
    
    /** Start a new IF piece. */
    private void startFromScratch()
    {
        this.editor.startFromScratch();
        this.insertTemplate( Templates.TEMPLATE_INI );
    }

    private final Editor editor;
    private final MainWindowView view;
}
