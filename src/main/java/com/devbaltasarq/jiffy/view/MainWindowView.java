// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/** The main window of Jiffy.
  * @author baltasarq
  */
public class MainWindowView extends JFrame {
    public MainWindowView()
    {
        this( null );
    }

    public MainWindowView(final Font DEFAULT_FONT)
    {
        this.font = DEFAULT_FONT;
        
        if ( this.font == null ) {
            this.font = new Font( Font.SANS_SERIF, Font.PLAIN, 14 );
        }
        
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.addWindowListener( this.windowListener );
        this.build();
        this.quitAction = () -> {};
        this.saveAction = () -> {};
        this.loadAction = () -> {};
        this.compileAction = () -> {};
        this.runAction = () -> {};
        this.insertLocAction = () -> {};
        this.insertObjAction = () -> {};
        this.helpAction = () -> {};
        this.aboutAction = () -> {};
        
        this.setLocationByPlatform( true );
        this.pack();
    }

    private void build()
    {
        final var SIZE = new Dimension( 620, 460 );
        final var SPLIT_PANEL = new JSplitPane();
        final BorderLayout LAY = new BorderLayout();
        
        LAY.setHgap( 10 );
        LAY.setVgap( 10 );
        
        this.setMinimumSize( SIZE );
        this.setSize( SIZE );
        this.setJMenuBar( this.buildMenuBar() );
        
        this.editor = new EditorView( this.font );
        this.tree = new LocTreeView( this.font );
        
        this.setLayout( LAY );
        this.add( SPLIT_PANEL, BorderLayout.CENTER );
        this.add( this.buildOutput(), BorderLayout.PAGE_END );
        SPLIT_PANEL.setLeftComponent( this.tree );
        SPLIT_PANEL.setRightComponent( this.editor );
        this.pack();
    }
    
    private JMenuBar buildMenuBar()
    {
        final var FILE = new JMenu( "File" );
        final var EDIT = new JMenu( "Edit" );
        final var TOOLS = new JMenu( "Tools" );
        final var HELP  = new JMenu( "Help" );
        
        // File
        this.opQuit = new JMenuItem( "Quit" );
        this.opQuit.setAccelerator(
                KeyStroke.getKeyStroke(
                                KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK ) );
        this.opLoad = new JMenuItem( "Open" );
        this.opLoad.setAccelerator(
                KeyStroke.getKeyStroke(
                                KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK ) );
        this.opSave = new JMenuItem( "Save" );
        this.opSave.setAccelerator(
                KeyStroke.getKeyStroke(
                                KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK ) );
        
        this.opExportToJson = new JMenuItem( "Export to json" );
        this.opExportToTrizbort = new JMenuItem( "Export to Trizbort" );
        
        FILE.add( this.opLoad );
        FILE.add( this.opSave );
        FILE.add( this.opExportToJson );
        FILE.add( this.opExportToTrizbort );
        FILE.add( this.opQuit );
        
        // Edit
        this.opInsertLoc = new JMenuItem( "Insert Loc" );
        this.opInsertLoc.setAccelerator(
                KeyStroke.getKeyStroke(
                                KeyEvent.VK_L,
                                KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK ) );
        this.opInsertObj = new JMenuItem( "Insert Obj" );
        this.opInsertObj.setAccelerator(
                KeyStroke.getKeyStroke(
                                KeyEvent.VK_O,
                                KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK ) );
        
        EDIT.add( this.opInsertLoc );
        EDIT.add( this.opInsertObj );
        
        // Tools
        this.opCompile = new JMenuItem( "Compile" );
        this.opCompile.setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_F6, 0 ));
        this.opRun = new JMenuItem( "Run" );
        this.opRun.setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ));
        
        TOOLS.add( this.opCompile );
        TOOLS.add( this.opRun );
        
        // Help
        this.opHelp = new JMenuItem( "Help" );
        this.opHelp.setAccelerator(
                KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ));
        this.opAbout = new JMenuItem( "About" );
        
        HELP.add( this.opHelp );
        HELP.add( this.opAbout );
        
        // Actions
        this.opLoad.addActionListener( (evt) -> this.loadAction.run() );
        this.opSave.addActionListener( (evt) -> this.saveAction.run() );
        this.opExportToJson.addActionListener( (evt) -> this.exportJsonAction.run() );
        this.opExportToTrizbort.addActionListener( (evt) -> this.exportTrizbortAction.run() );
        this.opQuit.addActionListener( (evt) -> this.quitAction.run() );
        this.opCompile.addActionListener( (evt) -> this.compileAction.run() );
        this.opRun.addActionListener( (evt) -> this.runAction.run() );
        this.opHelp.addActionListener( (evt) -> this.helpAction.run() );
        this.opAbout.addActionListener( (evt) -> this.aboutAction.run() );
        this.opInsertLoc.addActionListener( (evt) -> this.insertLocAction.run() );
        this.opInsertObj.addActionListener( (evt) -> this.insertObjAction.run() );

        // Build
        this.menuBar = new JMenuBar();        
        this.menuBar.add( FILE );
        this.menuBar.add( EDIT );
        this.menuBar.add( TOOLS );
        this.menuBar.add( HELP );
        return this.menuBar;
    }
    
    private JPanel buildOutput()
    {
        final var LY_OUTPUT = new BorderLayout();
        final var TORET = new JPanel( LY_OUTPUT );
        final var FONT = new Font( Font.MONOSPACED, Font.PLAIN, 18 );
                
        this.txtOutput = new JTextArea( 5, 80 );
        this.txtOutput.setFont( FONT );
        this.txtOutput.setEditable( false );
        
        LY_OUTPUT.setHgap( 5 );
        LY_OUTPUT.setVgap( 5 );
        
        this.txtOutput.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        TORET.add( new JScrollPane( this.txtOutput ), BorderLayout.CENTER );
        return TORET;
    }
    
    /** @return the output panel. */
    public JTextArea getOutput()
    {
        return this.txtOutput;
    }
    
    /** @return the default font. */
    public Font getDefaultFont()
    {
        return this.font;
    }
    
    /** @return the editor. */
    public EditorView getEditorView()
    {
        return this.editor;
    }
    
    /** @return the editor. */
    public LocTreeView getLocTreeView()
    {
        return this.tree;
    }
    
    /** Sets something to do when the window quits.
      * @param quitAction typically a lambda with something to do.
      */
    public void setQuitAction(Runnable quitAction)
    {
        this.quitAction = quitAction;
    }
    
    /** Sets something to do when the window quits.
      * @param loadAction typically a lambda with something to do.
      */
    public void setLoadAction(Runnable loadAction)
    {
        this.loadAction = loadAction;
    }
    
    /** Sets something to do when the window quits.
      * @param saveAction typically a lambda with something to do.
      */
    public void setSaveAction(Runnable saveAction)
    {
        this.saveAction = saveAction;
    }
    
    /** Sets something to do when the source is compiled.
      * @param compileAction typically a lambda with something to do.
      */
    public void setCompileAction(Runnable compileAction)
    {
        this.compileAction = compileAction;
    }
    
    /** Sets something to do when the source is run.
      * @param runAction typically a lambda with something to do.
      */
    public void setRunAction(Runnable runAction)
    {
        this.runAction = runAction;
    }
    
    /** Sets something to do when a loc is to be inserted.
      * @param insertLocAction typically a lambda with something to do.
      */
    public void setInsertLocAction(Runnable insertLocAction)
    {
        this.insertLocAction = insertLocAction;
    }
    
    /** Sets something to do when an object is to be inserted.
      * @param insertObjAction typically a lambda with something to do.
      */
    public void setInsertObjAction(Runnable insertObjAction)
    {
        this.insertObjAction = insertObjAction;
    }
    
    /** Sets something to do when 'about' is selected.
      * @param aboutAction typically a lambda with something to do.
      */
    public void setAboutAction(Runnable aboutAction)
    {
        this.aboutAction = aboutAction;
    }
    
    /** Sets something to do when 'help' is selected.
      * @param helpAction typically a lambda with something to do.
      */
    public void setHelpAction(Runnable helpAction)
    {
        this.helpAction = helpAction;
    }
    
    /** Sets something to do when 'export to JSON' is selected.
      * @param exportJsonAction typically a lambda with something to do.
      */
    public void setExportToJsonAction(Runnable exportJsonAction)
    {
        this.exportJsonAction = exportJsonAction;
    }
    
    /** Sets something to do when 'export to Trizbort' is selected.
      * @param exportTrizbortAction typically a lambda with something to do.
      */
    public void setExportToTrizbortAction(Runnable exportTrizbortAction)
    {
        this.exportTrizbortAction = exportTrizbortAction;
    }
    
    /** This is used for the window closing event. */
    private void onQuit()
    {
        this.quitAction.run();
    }
    
    private Font font;
    private EditorView editor;
    private LocTreeView tree;
    private JTextArea txtOutput;
    private JMenuBar menuBar;
    private JMenuItem opLoad;
    private JMenuItem opSave;
    private JMenuItem opExportToJson;
    private JMenuItem opExportToTrizbort;
    private JMenuItem opQuit;
    private JMenuItem opCompile;
    private JMenuItem opRun;
    private JMenuItem opInsertLoc;
    private JMenuItem opInsertObj;
    private JMenuItem opHelp;
    private JMenuItem opAbout;
    private Runnable quitAction;
    private Runnable loadAction;
    private Runnable saveAction;
    private Runnable exportJsonAction;
    private Runnable exportTrizbortAction;
    private Runnable compileAction;
    private Runnable runAction;
    private Runnable insertLocAction;
    private Runnable insertObjAction;
    private Runnable helpAction;
    private Runnable aboutAction;
    private final WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
            }

            @Override
            public void windowClosing(WindowEvent windowEvent)
            {
                MainWindowView.this.onQuit();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {}

            @Override
            public void windowIconified(WindowEvent windowEvent) {}

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {}

            @Override
            public void windowActivated(WindowEvent windowEvent) {}

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {}
        };
}
