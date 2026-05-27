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
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.Parser;
import com.devbaltasarq.jiffy.core.SourceFile;
import com.devbaltasarq.jiffy.core.ast.JsonWriter;
import com.devbaltasarq.jiffy.core.ast.TrizbortWriter;
import com.devbaltasarq.jiffy.core.emitter.CompleteFiJsEmitter;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.errors.EmitError;
import com.devbaltasarq.jiffy.core.parser.Template;
import java.io.File;
import java.util.Map;


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
        this.view.setInsertLocAction( () -> this.doInsertLoc() );
        this.view.setInsertObjAction( () -> this.doInsertObj() );
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
        
        this.setWindowTitle();
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
        this.show(  "[WAN] Starting from scratch." );
        this.startFromScratch();
        this.setWindowTitle();
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
                final File SELECTED_FILE = FILE_DLG.getSelectedFile();
                
                this.show( "[I/O] Loading: '" + SELECTED_FILE + "'" );
                this.getEditor().loadFromPath( SELECTED_FILE.toPath() );
                this.setWindowTitle();
            } catch(IOException exc)
            {
                this.show( "[I/O] Error: " + exc.getMessage() );
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
                                        "Text files (*.txt)",
                                        "txt" ) );

        FILE_CHOOSER.setFileFilter(
                        new FileNameExtensionFilter(
                                        "Jiffy files (*.jiffy)",
                                        "jiffy" ) );
    }
    
    private Path askPath()
    {
        final var FILE_CHOOSER = new JFileChooser();
        Path toret = null;
        
        this.configure( FILE_CHOOSER );
        
        int result = FILE_CHOOSER.showSaveDialog( this.getView() );
        if ( result == JFileChooser.APPROVE_OPTION ) {
            toret = FILE_CHOOSER.getSelectedFile().toPath();
        }
        
        return toret;
    }
    
    /** Saves the document from the editor. */
    private void doSave()
    {
        if ( !this.editor.hasDocument() ) {
            this.editor.assignPath( this.askPath() );
        }
        
        if ( this.editor.hasDocument() ) {
            try {
                this.show( "[I/O] Saving: '" + this.editor.getPath() + "'" );
                this.editor.save();
                this.setWindowTitle();
            } catch(IOException exc) {
                this.show( "[I/O] Error: " + exc.getMessage() );
                
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
        this.doSave();
        this.getView().setVisible( false );
        System.exit( 0 );
    }
    
    /** Insert a new loc. */
    private void doInsertLoc()
    {
        this.clearOutput();
        this.show( "[INF] Inserting loc: asking for its name" );
        
        final String LOC_NAME = JOptionPane.showInputDialog(
                            this.getView(),
                            "Name for the new Loc?",
                            AppInfo.NAME,
                            JOptionPane.OK_CANCEL_OPTION );
        
        if ( LOC_NAME != null
          && !LOC_NAME.isBlank() )
        {
            final String LOC_ID = Id.varNameFromId( "", LOC_NAME ).toLowerCase();
            final Template LOC_TEMPLATE = new Template( Template.TEMPLATE_LOC );
            final Map<String, String> SUBSTS = Map.of(
                                                "LOC_ID", LOC_ID,
                                                "LOC_NAME", LOC_NAME,
                                                "LOC_PIC", LOC_ID,
                                                "LOC_DESC", "Beautiful loc." );

            this.show( "[INF] Inserting '" + LOC_ID + "' / '" + LOC_NAME );            
            this.insertTemplate(LOC_TEMPLATE.applySubsts( SUBSTS ) );
        } else {
            this.show( "[ERR] No valid name given." );
        }
    }
    
    private void doInsertObj()
    {
        this.clearOutput();
        this.show( "[INF] Inserting obj: asking for its name" );
        
        final String OBJ_NAME = JOptionPane.showInputDialog(
                            this.getView(),
                            "Name for the new Obj?",
                            AppInfo.NAME,
                            JOptionPane.OK_CANCEL_OPTION );
        
        if ( OBJ_NAME != null
          && !OBJ_NAME.isBlank() )
        {
            final String OBJ_ID = Id.varNameFromId( "", OBJ_NAME ).toLowerCase();
            final Template OBJ_TEMPLATE = new Template( Template.TEMPLATE_OBJ );
            final Map<String, String> SUBSTS = Map.of(
                                                "OBJ_ID", OBJ_ID,
                                                "OBJ_NAME", OBJ_NAME,
                                                "OBJ_DESC", "Beautiful thing." );

            this.show( "[INF] Inserting '" + OBJ_ID + "' / '" + OBJ_NAME );            
            this.insertTemplate(OBJ_TEMPLATE.applySubsts( SUBSTS ) );
        } else {
            this.show( "[ERR] No valid name given." );
        }
    }
    
    /** Compile the source code. */
    private void doCompile()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No file path assigned yet.",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        this.doSave();
        
        if ( !this.getEditor().hasDocument() ) {
            return;
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
                
        try {
            final Parser PARSE = new Parser();
            
            this.clearOutput();
            this.show( "[CMP] " + path );
            
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "[EMT] " + TARGET_FILE );
            new CompleteFiJsEmitter( AST, TARGET_FILE.getAbsolutePath() ).emit();
        } catch(CompileError | EmitError | IOException exc) {
            this.show( "[ERR] " + exc.getMessage() );
        }
    }
    
    /** Execute the source code. */
    private void doRun()
    {
        if ( !this.getEditor().hasDocument() ) {
            JOptionPane.showMessageDialog(
                            this.getView(),
                            "No file path assigned yet.",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        this.doSave();
        this.doCompile();
        
        if ( !this.getEditor().hasDocument() ) {
            return;
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = new File( SOURCE.buildTarget().
                                                getAbsolutePath() + ".html" );

        try {
            this.clearOutput();
            this.show( "[RUN] " + TARGET_FILE );
                        
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
            this.show( "[I/O] Unsupported action: " + exc.getMessage() );
        } catch(IOException exc) {
            this.show( "[I/O] I/O Error: " + exc.getMessage() );
        } catch(Exception exc) {
            this.show( "[ERR] Unexpected error: " + exc.getMessage() );
        }
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
        final String URL_WIKI = AppInfo.WIKI_WEB_SITE;
        
        try {
            this.clearOutput();
            
            if ( !Desktop.isDesktopSupported() ) {
                throw new IOException( "desktop not supported" );
            }
                        
            final var DESKTOP = Desktop.getDesktop();
            
            if ( !DESKTOP.isSupported( Desktop.Action.BROWSE ) ) {
                throw new IOException( "Cannot open web browser for: " + URL_WIKI );
            }
            
            DESKTOP.browse( new URI( URL_WIKI ) );
        } catch(URISyntaxException | IOException exc)
        {
            this.show( "[I/O] Error: " + exc.getMessage() );
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
                            "No file path assigned yet.",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        this.doSave();
        
        if ( !this.getEditor().hasDocument() ) {
            return;
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
        
        this.clearOutput();
        
        try {
            final Parser PARSE = new Parser();
            
            this.show( "[CMP] " + path );
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "[I/O] Saving: " + SOURCE.buildTarget() + ".json" );
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
                            "No file path assigned yet.",
                            AppInfo.NAME,
                            JOptionPane.ERROR_MESSAGE );
        }
        
        this.doSave();
        
        if ( !this.getEditor().hasDocument() ) {
            return;
        }
        
        var path = this.getEditor().getPath().toFile();
        final SourceFile SOURCE = new SourceFile( path );
        final File TARGET_FILE = SOURCE.buildTarget();
        
        this.doSave();
        this.clearOutput();
        
        try {
            final Parser PARSE = new Parser();
            
            this.show( "[CMP] " + path );
            final AST AST = PARSE.parseFile( SOURCE.get().getAbsolutePath() );
            
            this.show( "[I/O] Saving Trizbort: " + SOURCE.buildTarget() + ".json" );
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
        this.insertTemplate(Template.TEMPLATE_INI );
    }
    
    /** Correctly sets the title of the window. */
    private void setWindowTitle()
    {
        String title = "";
        
        if ( this.getEditor().hasDocument() ) {
            title += this.getEditor().getPath().getFileName() + " - ";
        }
        
        this.getView().setTitle( title + AppInfo.NAME );
    }

    private final Editor editor;
    private final MainWindowView view;
}
