package core;// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


public final class Util {
    /** Removes the extension of this file name (removes from last '.').
      * @param fileName the file name to remove the extension from.
      * @return the same file name, but without the extension.
      */
    public static String removeExt(String fileName)
    {
        int pos = fileName.lastIndexOf( '.' );

        if ( pos > -1 ) {
            fileName = fileName.substring( 0, pos );
        }

        return fileName;
    }

    /** Converts an id, such as "salón" in an id
      * that can safely be used for a variable or
      * even for a file name.
      * @param prefix a prefix to prepend to the new id.
      * @param id the id such, as "salón"
      * @return the converted id, i.e. prefix + "salon".
     */
    public static String varNameFromId(String prefix, String id)
    {
        final String ACUTED_UPPER_VOWELS = "\u00C1\u00C9\u00CD\u00D3\u00DA";
        final String VOWELS = "AEIOU";
        final StringBuilder TORET =
                new StringBuilder( id.length() + prefix.length() );
        String strId = id.trim().toUpperCase();

        // Add prefix, if needed
        prefix = prefix.trim();
        if ( !prefix.isEmpty() ) {
            TORET.append( prefix );
            TORET.append( '_' );
        }

        // Add the id, replacing vowels and spaces
        strId.replace( "  ", "_" );
        for(char ch: strId.toCharArray()) {
            int vowelPos = ACUTED_UPPER_VOWELS.indexOf( ch );

            if ( vowelPos > -1 ) {
                TORET.append( VOWELS.charAt( vowelPos ) );
            }
            else
                // Only ascii
                if ( ch >= 'A'
                  && ch <= 'Z' )
                {
                    TORET.append( ch );
                }
        }

        return TORET.toString();
    }

    public static String divideInLinesWith(int cols, String txt, String delimiter)
    {
        final StringBuilder TORET = new StringBuilder( txt.length() );
        int pos = 0;
        int nextCol = cols;

        while( nextCol > pos
            && nextCol < txt.length() )
        {
            if ( txt.charAt( nextCol ) == ' ' ) {
                TORET.append( txt, pos, nextCol );
                TORET.append( delimiter );

                pos = nextCol;
                nextCol += cols;
            } else {
                --nextCol;
            }
        }

        TORET.append( txt.substring( pos ) );
        return TORET.toString();
    }
}
