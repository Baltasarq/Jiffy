// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import com.devbaltasarq.jiffy.core.errors.CompileError;


/** Represents the id's for entities. */
public class Id {
    public static final String EMPTY_STORY_ID = "??";

    public Id(String strId) throws CompileError
    {
        if ( strId == null ) {
            throw new Error( "trying to build Id with a null" );
        }

        strId = strId.trim();

        if ( strId.isEmpty() ) {
            throw new CompileError( "trying to create an empty id" );
        }

        this.id = idFromTxt( strId );
    }

    public String get()
    {
        return this.id;
    }

    @Override
    public int hashCode()
    {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        boolean toret = false;

        try {
            if ( other instanceof final Id OTHER_ID ) {
                toret = this.get().equals( OTHER_ID.get() );
            }
            else
            if ( other instanceof final String OTHER_STR ) {
                toret = new Id( OTHER_STR ).get().equals( this.get() );
            }
        } catch(CompileError exc) {
            toret = false;
        }

        return toret;
    }

    @Override
    public String toString()
    {
        return this.get();
    }

    private static String idFromTxt(String txt)
    {
        return varNameFromId( "", txt.trim() ).toLowerCase();
    }

    public static Id empty()
    {
        if ( emptyId == null ) {
            try {
                emptyId = new Id( EMPTY_STORY_ID );
            } catch(CompileError exc) {
                throw new Error( "creating empty_id" );
            }
        }

        return emptyId;
    }
    
    /** Converts a name, such as "salón" in an id
      * that can safely be used for a variable or
      * even for a file name.
      * @param prefix a prefix to prepend to the new id.
      * @param name the name such as "salón"
      * @return the converted id, i.e. prefix + "salon".
     */
    public static String varNameFromId(String prefix, String name)
    {
        final String ACUTED_UPPER_VOWELS = "\u00C1\u00C9\u00CD\u00D3\u00DA";
        final String VOWELS = "AEIOU";
        final StringBuilder TORET =
                new StringBuilder( name.length() + prefix.length() );
        String strId = name.trim().toUpperCase();

        // Add prefix, if needed
        prefix = prefix.trim();
        if ( !prefix.isEmpty() ) {
            TORET.append( prefix );
            TORET.append( '_' );
        }

        // Add the id, replacing vowels and spaces
        strId = strId.replaceAll( "  ", "_" );
        for(char ch: strId.toCharArray()) {
            int vowelPos = ACUTED_UPPER_VOWELS.indexOf( ch );

            if ( vowelPos > -1 ) {
                TORET.append( VOWELS.charAt( vowelPos ) );
            } else {
                // Only underscores, ascii letters and numbers
                boolean addCh =
                            ( ch >= 'A'
                           && ch <= 'Z' )
                        ||  ( ch >= '0'
                           && ch <= '9' )
                        ||  ( ch == '_' );
                
                if ( addCh ) {
                    TORET.append( ch );
                }
            }
        }

        return TORET.toString();
    }

    private static Id emptyId;
    private final String id;
}
