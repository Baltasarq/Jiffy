// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;


/** The editor for the story.
  * @author baltasarq
  */
public class Editor {
    final static Logger LOG = Logger.getLogger( Editor.class.getSimpleName() );
    
    public Editor()
    {
        this( new EditorView() );
    }
    
    public Editor(EditorView editorView)
    {
        this.view = editorView;
        this.path = null;
    }
    
    /** @return the view of the editor. */
    public EditorView getView()
    {
        return this.view;
    }
    
    /** @return gets the path of the document. */
    public Path getPath()
    {
        return this.path;
    }
    
    /** Sets the editor to the "no document" state. */
    public void setNoDocument()
    {
        this.getView().getEditor().setText( "" );
        this.path = null;
        this.hasDocument = false;
    }
    
    /** @return true if there is a document, false otherwise. */
    public boolean hasDocument()
    {
        return this.hasDocument;
    }
    
    /** Sets the path of the document being edited.
      * @param path the path of the document, or null for no document.
      * @throws IOException if reading goes wrong.
      */
    public void loadFromPath(Path path) throws IOException
    {
        this.path = path;
        
        if ( path != null ) {
            try {
                this.getView().getEditor().setText(
                    Files.readString( path ) );
                this.hasDocument = true;
            } catch(IOException exc) {
                LOG.severe( "load(): could not open: " + path );
                this.setNoDocument();
                throw new IOException( exc );
            }
        }
    }
    
    /** Saves the current document using the current path.
      * @throws IOException if the writing goes wrong.
      */
    public void save() throws IOException
    {
        if ( this.getPath() != null ) {
            LOG.info( "saving: " + this.getPath() );

            Files.writeString(
                this.getPath(),
                this.getView().getEditor().getText(),
                Charset.forName( StandardCharsets.UTF_8.name() ),
                StandardOpenOption.CREATE );
        } else {
            LOG.warning( "save() called, but no document in the editor!!" );
        }
    }
    
    /** It holds a document, but it wasn't saved yet. */
    public void startFromScratch()
    {
        this.setNoDocument();
        this.hasDocument = true;
    }
    
    private Path path;
    private boolean hasDocument;
    private EditorView view;
}
