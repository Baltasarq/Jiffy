// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;


/** The view for the story editor.
  * @author baltasarq
  */
public class EditorView extends JPanel {
    public EditorView()
    {
        this( null );
    }
    
    public EditorView(Font font)
    {
        this.textEditor = new JTextArea();
        
        if ( font == null ) {
            this.font = new Font( Font.MONOSPACED, Font.PLAIN, 18 );
        } else {
            this.font = new Font( Font.MONOSPACED, Font.PLAIN, font.getSize() + 4 );
        }
        
        this.build();
    }
    
    private void build()
    {
        final BorderLayout LAY = new BorderLayout();
        
        LAY.setHgap( 10 );
        LAY.setVgap( 10 );
        
        this.setLayout( LAY );
        this.textEditor.setFont( this.font );
        this.add( new JScrollPane( this.textEditor ), BorderLayout.CENTER );
    }
    
    /** @return the text stored in the editor. */
    public String getText()
    {
        return this.textEditor.getText();
    }
    
    /** @return the main editor component itself. */
    public JTextArea getEditor()
    {
        return this.textEditor;
    }
    
    private Font font;
    private final JTextArea textEditor;
}
