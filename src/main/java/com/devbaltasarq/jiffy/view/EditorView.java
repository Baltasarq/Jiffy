// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Style;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.TokenTypes;
import org.fife.ui.rtextarea.RTextScrollPane;


/** The view for the story editor.
  * @author baltasarq
  */
public class EditorView extends JPanel {
    final static Logger LOG = Logger.getLogger( Editor.class.getSimpleName() );
    
    public EditorView()
    {
        this( null );
    }
    
    public EditorView(Font font)
    {
        final var tmf = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        tmf.putMapping( "text/custom-markdown",
                        "com.devbaltasarq.jiffy.view.editor.CustomMarkdownTokenMaker" );

        this.textEditor = new RSyntaxTextArea();
        this.textEditor.setLineWrap( true );
        this.textEditor.setWrapStyleWord( true );
        this.textEditor.setSyntaxEditingStyle( "text/custom-markdown" );
        this.textEditor.setAnimateBracketMatching( true );
        this.textEditor.setAutoIndentEnabled( false );
        this.textEditor.setBracketMatchingEnabled( true );
        this.textEditor.setCodeFoldingEnabled( false );
                
        if ( font == null ) {
            this.font = new Font( Font.MONOSPACED, Font.PLAIN, 18 );
        } else {
            this.font = new Font( Font.MONOSPACED, Font.PLAIN, font.getSize() + 4 );
        }
        
        this.build();
    }
    
    private void build()
    {
        final var SCROLL_PANE = new RTextScrollPane( this.textEditor );
        final var SCHEME = this.textEditor.getSyntaxScheme();
        final Style REF_STYLE = SCHEME.getStyle( TokenTypes.MARKUP_TAG_NAME );
        final Style ENTITY_STYLE = SCHEME.getStyle( TokenTypes.MARKUP_ENTITY_REFERENCE );
        final Style VAR_STYLE = SCHEME.getStyle( TokenTypes.VARIABLE );
        
        REF_STYLE.foreground = Color.decode( "#00008B" );
        REF_STYLE.font = this.font.deriveFont( Font.BOLD );
        
        ENTITY_STYLE.foreground = Color.decode( "#008B00" );
        ENTITY_STYLE.font = this.font.deriveFont( Font.BOLD );
        
        VAR_STYLE.foreground = Color.decode( "#0000C0" );
        VAR_STYLE.font = this.font.deriveFont( Font.BOLD );

        this.textEditor.setFont( this.font );
        this.setLayout( new BorderLayout() );
        this.add( SCROLL_PANE, BorderLayout.CENTER );
    }
    
    /** @return the text stored in the editor. */
    public String getText()
    {
        return this.textEditor.getText();
    }
    
    /** @return the main editor component itself. */
    public RSyntaxTextArea getEditor()
    {
        return this.textEditor;
    }
    
    /** Move the editor to a given line.
      * @param lineIndex the index of the line to move to.
      */
    public void goToLine(int lineIndex)
    {
        try {
           int startOffset = this.getEditor().getLineStartOffset( lineIndex );

           this.getEditor().setCaretPosition( startOffset );
       } catch (BadLocationException exc) {
           LOG.warning( "Bad offset while moving to line: "
                            + lineIndex
                            + ": " + exc.getMessage() );
       }
    }
    
    private Font font;
    private final RSyntaxTextArea textEditor;
}
