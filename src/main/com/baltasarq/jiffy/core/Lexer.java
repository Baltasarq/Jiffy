// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core;


/** Lexer for a given String */
public final class Lexer {
    public static final char COMMENT = '#';
    public static final char ASSIGN = '=';
    public static final char VAR = '@';
    public static final char OPENED_SQ_BRACKET = '[';
    public static final char CLOSED_SQ_BRACKET = ']';
    public static final String OPEN_LOC = "" + OPENED_SQ_BRACKET + OPENED_SQ_BRACKET;
    public static final String CLOSE_LOC = "" + CLOSED_SQ_BRACKET + CLOSED_SQ_BRACKET;
    public static final String DELIMITERS = "" + OPENED_SQ_BRACKET + CLOSED_SQ_BRACKET;
    public static final String SPECIAL_CHARACTER = COMMENT + DELIMITERS;
    public static final String BOOLEAN_VALUES = " true false ";
    public enum TokenType {ID, BOOL, NUMBER, STR, SPECIAL_CHAR, INVALID}

    public Lexer(String ln)
    {
        this.line = ln.trim();
        this.pos = 0;
    }

    public void advance()
    {
        advance( 1 );
    }

    public void advance(int value)
    {
        this.setPos( this.getPos() + value );
    }

    public boolean isWhiteSpace()
    {
        return ( Character.isWhitespace( this.getCurrentChar() ) );
    }

    public void skipSpaces()
    {
        while( !this.isEOL()
            && this.isWhiteSpace() )
        {
            this.advance();
        }

        return;
    }

    public boolean isEOL()
    {
        return ( this.getPos() >= getLine().length() );
    }

    public String getToken()
    {
        char ch;

        token = "";
        this.skipSpaces();
        ch = this.getCurrentChar();


        while( ( Character.isLetterOrDigit( ch )
              || ch == '_' )
              && this.getPos() < this.getLine().length() )
        {
            token += ch;
            advance();
            ch = getCurrentChar();
        }

        this.skipSpaces();
        return token;
    }

    /**
     * Reads a number in the source, in the format: (9)+[.(9)+]
     * @return The number read, as a string.
     */
    public String getNumberLiteral()
    {
        token = "";
        skipSpaces();
        char ch = getCurrentChar();

        // Read the first sign, if exists
        if ( ch == '+'
                || ch == '-' )
        {
            token += ch;
            this.advance();
            ch = getCurrentChar();
        }

        while( Character.isDigit( ch )
            || ch == '.' )
        {
            token += ch;
            this.advance();
            ch = getCurrentChar();
        }

        return token;
    }

    public String getStringLiteral()
    {
        return getLiteral( '"', '"' );
    }

    public String getLiteral(char openDelimiter, char endDelimiter)
    {
        token = "";
        skipSpaces();

        if ( getCurrentChar() == openDelimiter ) {
            advance();

            while( getCurrentChar() != endDelimiter
                && !this.isEOL() )
            {
                if ( getCurrentChar() != '\\' ) {
                    token += getCurrentChar();
                } else {
                    advance();

                    if ( !this.isEOL() ) {
                        char ch = getCurrentChar();

                        switch( ch ) {
                            case 'n': ch = '\n'; break;
                            case 't': ch = '\t'; break;
                            case '\"': ch = '\"'; break;
                            case '\'': ch = '\''; break;
                            case '\\': ch = '\\'; break;
                            default: ch = '?' ; break;
                        }

                        token += ch;
                    }
                }

                this.advance();
            }

            this.advance();
        }

        this.skipSpaces();
        return token;
    }

    public char getCurrentChar()
    {
        char toret = '\0';

        if ( !this.isEOL() ) {
            toret = this.getLine().charAt( this.getPos() );
        }

        return toret;
    }

    public TokenType getNextTokenType()
    {
        TokenType toret = TokenType.INVALID;
        char ch;

        // Chk first char for literals
        this.skipSpaces();
        ch = this.getCurrentChar();

        if ( ch == '"' ) {
            toret = TokenType.STR;
        }
        else
        if ( SPECIAL_CHARACTER.indexOf( ch ) > -1 ) {
            toret = TokenType.SPECIAL_CHAR;
        } else {
            int oldPos = getPos();

            if ( Character.isDigit( ch )
              || ch == '+'
              || ch == '-' )
            {
                String strNum = this.getNumberLiteral();

                if ( isNumber( strNum ) ) {
                    toret = TokenType.NUMBER;
                }
            } else {
                this.getToken();

                if ( isBool( getCurrentToken() ) ) {
                    toret = TokenType.BOOL;
                } else {
                    toret = TokenType.ID;
                }
            }

            this.pos = oldPos;
        }

        this.token = "";
        return toret;
    }

    /** Determines wether the token is a number or not
     * @param token A string to compare
     * @return true when number, false otherwise
     */
    public static boolean isNumber(String token) {
        boolean toret = true;

        try {
            Double.parseDouble(token);
        }
        catch(Exception e)
        {
            toret = false;
        }

        return toret;
    }

    /**
     * Decides whether the token holds a string literal or not
     * @param token The string to decide on the format: "text"
     * @return true if it holds text delimited by double-quotes, false otherwise
     */
    public static boolean isStringLiteral(String token)
    {
        return isLiteral( '"', '"', token );
    }

    /**
     * Decides whether the token holds a literal or not
     * @param token The string to decide on the format: <delimiter>literal<delimiter>
     * @return true if it holds text delimited by delimiter, false otherwise
     */
    public static boolean isLiteral(char openDelimiter, char endDelimiter, String token)
    {
        return ( token.charAt( 0 ) == openDelimiter
                && token.charAt( token.length() -1 ) == endDelimiter )
                ;
    }

    /**
     * Decides whether the token is a boolean or not.
     * @param token A string containing "true", "false", or any other value.
     * @return true if it is "true" or "false", false otherwise
     */
    public static boolean isBool(String token) {
        return ( BOOLEAN_VALUES.contains( " " + token + " " ) );
    }

    /**
     * Determines whether the string is an identifier
     * @param token The identifier to check
     * @return true if it is an identifier, false otherwise
     */
    public static boolean isIdentifier(String token)
    {
        boolean toret = false;

        token = token.trim();

        if ( !token.isEmpty() ) {
            final char firstChar = token.charAt( 0 );
            final char lastChar = token.charAt(  token.length() -1 );

            if ( Character.isLetter( firstChar )
              || firstChar == '_' )
            {
                final int len = token.length() -1;
                int i = 1;

                for(; i < len; ++i) {
                    final char ch = token.charAt( i );

                    if ( !Character.isLetterOrDigit( ch )
                      && ch != '_' )
                    {
                        break;
                    }
                }

                toret = ( i >= len );    // in case length is 1: "x"
            }
        }

        return toret;
    }

    public boolean isDelimiter()
    {
        return isDelimiter( this.getCurrentChar() );
    }

    public static boolean isDelimiter(char ch)
    {
        return ( DELIMITERS.indexOf( ch ) > -1 );
    }

    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * @return the line
     */
    public String getLine() {
        return line;
    }

    /**
     * @return the current token
     */
    public String getCurrentToken() {
        return token;
    }

    /**
     * Inserts a new particle at the current position of the lexed string.
     * @param part A new part to insert, as a String.
     */
    public void insertAtCurrentPos(String part)
    {
        StringBuffer newLine = new StringBuffer( this.getLine() );

        newLine.insert( this.getPos(), part + ' ' );
        this.line = newLine.toString();
    }

    public boolean match(char ch)
    {
        boolean toret = false;

        this.skipSpaces();

        if ( this.getCurrentChar() == ch ) {
            toret = true;
            this.advance();
        }

        return toret;
    }

    public boolean match(String txt)
    {
        boolean toret = true;

        this.skipSpaces();

        int oldPos = this.getPos();
        for(int i = 0; i < txt.length(); ++i) {
            if ( txt.charAt( i ) != this.getCurrentChar() ) {
                toret = false;
                this.setPos( oldPos );
                break;
            }

            this.advance();
        }

        return toret;
    }

    private int pos;
    private String line;
    private String token;
}
