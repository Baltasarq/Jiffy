// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import java.io.File;


/** Represents a source file, from which the target file can be obtained.
  */
public class SourceFile {
    public SourceFile(String fn)
    {
        this( new File( fn ) );
    }
    
    public SourceFile(File f)
    {
        assert f != null: "source file cannot be null";
        this.source = f;
    }
    
    /** @return the source file. */
    public File get()
    {
        return this.source;
    }
    
    /** @return the target file. */
    public File buildTarget()
    {
        if ( this.target == null ) {
            this.target = new File( this.removeExt( this.source.getAbsolutePath() ));
        }
        
        return this.target;
    }
    
    /** Removes the extension of this file name (removes from last '.' on).
      * @param fileName the file name to remove the extension from.
      * @return the same file name, but without the extension.
      */
    private String removeExt(String fileName)
    {
        int pos = fileName.lastIndexOf( '.' );

        if ( pos > -1 ) {
            fileName = fileName.substring( 0, pos );
        }

        return fileName;
    }
    
    private File target;
    private final File source;
}
