// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view.editor;


import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.AbstractTokenMaker;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMap;
import org.fife.ui.rsyntaxtextarea.TokenTypes;


public class CustomMarkdownTokenMaker extends AbstractTokenMaker {
    @Override
    public Token getTokenList(
                        Segment text,
                        int initialTokenType,
                        int startOffset)
    {
        char[] array = text.array;
        int offset = text.offset;
        int end = offset + text.count;
        int newStartOffset = startOffset - offset;
        int i = offset;

        this.resetTokenList();        
        while ( i < end ) {
            // Look for variable
            if ( array[ i ] == '@' ) {
                int start = i;
                i += 1;
                
                // Find var closing
                while ( i < end
                     && array[ i ] != ' '
                     && array[ i ] != '=' )
                {
                    ++i;
                }
                
                if ( i < end ) {
                    i += 1;
                }

                this.addToken(
                        text,
                        start,
                        i - 1,
                        TokenTypes.VARIABLE,
                        newStartOffset + start );

                continue;
            }

            // Look for [ ... ]
            if ( array[ i ] == '[' ) {
                // Found opening square bracket
                int tokenType = TokenTypes.MARKUP_ENTITY_REFERENCE;
                int start = i;
                i += 1;
                
                if ( i < end
                  && array[ i ] == '[' )
                {
                    tokenType = TokenTypes.MARKUP_TAG_NAME;
                    i += 1;
                }
                
                // Find closing ]
                while ( i < end
                     && array[ i ] != ']' )
                {
                    ++i;
                }
                
                if ( i < end ) {
                    i += 1;
                }
                    
                if ( i < end
                  && array[ i ] == ']' )
                {
                    i += 1;
                }
                    
                this.addToken(
                        text,
                        start,
                        i - 1,
                        tokenType,
                        newStartOffset + start );
                
                continue;
            }
            
            // Find start of normal text segment
            int textStart = i;

            // Advance until we hit another [ or end of line
            while ( i < end
                 && array[ i ] != '[' )
            {
                i++;
            }

            if ( i > textStart ) {
                this.addToken(
                        text,
                        textStart,
                        i - 1,
                        TokenTypes.IDENTIFIER,
                        newStartOffset + textStart );

                ++offset;
            }
        }

        // Add end-of-line token
        this.addNullToken();
        return this.firstToken;
    }

    /** Returns the words to highlight. */
    @Override
    public TokenMap getWordsToHighlight() {
        final var TORET = new TokenMap();
        
        TORET.put( "@syn", TokenTypes.VARIABLE );
        TORET.put( "@author", TokenTypes.VARIABLE );
        TORET.put( "@version", TokenTypes.VARIABLE );
        return TORET;
    }    
}
