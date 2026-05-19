// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.ast.Loc;
import com.devbaltasarq.jiffy.core.ast.Obj;
import com.devbaltasarq.jiffy.core.ast.Direction;
import com.devbaltasarq.jiffy.core.errors.CompileError;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


/** Markdown To HTML converter. */
public class HtmlFromMarkdown {
    private final char CTRL_HIGHLIGHT = '\u0081';
    private final char CTRL_BOLD = '\u008D';
    private final char CTRL_MOV = '\u008F';
    private final String REF_PATTERN = "${$TEXT, ex $REF}";
    private final String MOV_PATTERN = "${$TEXT, $DIR}";

    /** Creates a new converter, with the given text to convert.
      * @param ENT the entity with a desc to convert.
      */
    public HtmlFromMarkdown(final Entity ENT)
    {
        // Init tags dictionaries
        if ( openTags == null
          || closeTags == null
          || chrStates == null )
        {
            final TokenType[] STATS = {
                    TokenType.ITALIC,
                    TokenType.BOLD,
                    TokenType.HIGHLIGHT,
                    TokenType.CODE
            };
            final String[] OPEN_TAGS = {
                    "<i>",
                    "<b>",
                    "<b><i>",
                    "<code>"
            };
            final String[] CLOSE_TAGS = {
                    "</i>",
                    "</b>",
                    "</i></b>",
                    "</code>"
            };

            openTags = new HashMap<>( STATS.length );
            closeTags = new HashMap<>( STATS.length );

            // Create status-tag maps
            for(int i = 0; i < STATS.length; ++i) {
                openTags.put( STATS[ i ], OPEN_TAGS[ i ] );
                closeTags.put( STATS[ i ], CLOSE_TAGS[ i ] );
            }

            // Create char-status map
            final char[] CHRS = {
                    CTRL_HIGHLIGHT, CTRL_BOLD,
                    CTRL_MOV, '*',
                    '[', ']', '`'
            };

            final TokenType[] STATS_FOR_CHRS = {
                    TokenType.HIGHLIGHT, TokenType.BOLD,
                    TokenType.MOVEMENT, TokenType.ITALIC,
                    TokenType.REFERENCE, TokenType.REFERENCE, TokenType.CODE
            };

            chrStates = new HashMap<>( CHRS.length );

            for(int i = 0; i < CHRS.length; ++i) {
                chrStates.put( CHRS[ i ], STATS_FOR_CHRS[ i ] );
            }
        }

        // Init text, replacing marks of length > 1
        this.ENT = ENT;
        this.TEXT = ENT.getDesc().trim()
                .replace( "***", Character.toString( CTRL_HIGHLIGHT ) )
                .replace( "**", Character.toString( CTRL_BOLD ) )
                .replace( "___", Character.toString( CTRL_HIGHLIGHT ) )
                .replace( "__", Character.toString( CTRL_BOLD ) )
                .replace( "[[", Character.toString( CTRL_MOV ) )
                .replace( "]]", Character.toString( CTRL_MOV ) );
    }
    
    /** Converts a reference.
      * For instance: - [stairs|the stairs]      ${the stairs, ex stairs}
      * @param LEX the lexer the lexer for the source code.
      * @param TORET the stringbuilder with the result.
      * @throws CompileError if the source is not correct.
      */
    private void convertReference(final Lexer LEX,
                                  final StringBuilder TORET)
                   throws CompileError
    {
        LEX.advance();

        String litRef = LEX.getToken();
        String ref = Id.varNameFromId( "", litRef )
                       .toLowerCase();
        String text = "";

        // The long text
        if ( LEX.getCurrentChar() == '|' ) {
            text = LEX.getLiteral( '|', ']' );
            LEX.advance( -1 );
        }

        if ( !LEX.match( "]" ) ) {
            throw new CompileError( "missing ]" );
        }

        LEX.advance( -1 );

        // Append the reference pattern: ${REF, ex REF}
        if ( text.isEmpty() ) {
            text = litRef;
        }

        TORET.append( REF_PATTERN
            .replace( "$TEXT", text )
            .replace( "$REF", ref ) );
    }
    
    /** Converts a movement.
      * For instance: [[kitchen|the kitchen]](e).     ${the kitchen, e}
      * @param LEX the lexer for the source code.
      * @param TORET the StringBuilder for the result.
      * @throws CompileError if the source code is wrong.
      */
    private void convertMovement(final Lexer LEX, final StringBuilder TORET)
                 throws CompileError
    {
        LEX.advance();

        String ref = "";
        String text = "";
        String strDir = "";
        Direction dir = null;
        String litRef = LEX.getToken();

        ref = Id.varNameFromId( "", litRef )
                .toLowerCase();

        // The long text
        if ( LEX.getCurrentChar() == '|' ) {
            text = LEX.getLiteral( '|', CTRL_MOV );
            LEX.advance( -1 );
        }

        if ( !LEX.match( CTRL_MOV ) ) {
            throw new CompileError( "missing ]]" );
        }

        // The direction
        if ( LEX.getCurrentChar() == '(' ) {
            LEX.advance();
            strDir = LEX.getToken();
            dir = Direction.of( strDir );
            LEX.skipSpaces();

            if ( !LEX.match( ')' )) {
                throw new CompileError( "missing ')'" );
            }
        } else {
            throw new CompileError( "missing link direction" );
        }

        LEX.advance( -1 );
        if ( text.isEmpty() ) {
            text = litRef;
        }

        Loc locThis = null;
        Id destLocId = null;

        if ( this.getEntity() instanceof final Loc LOC ) {
            locThis = LOC;
        } else {
            locThis = ( (Obj) this.getEntity() ).getOwner();
        }

        // Create the id for the destination loc
        try {
            destLocId = new Id( ref );
        } catch(CompileError exc) {
            throw new CompileError( "link: incorrect dest loc id: " + ref );
        }
        
        locThis.setExitAt( dir, destLocId );

        TORET.append( MOV_PATTERN
                .replace( "$TEXT", text )
                .replace( "$DIR", strDir ) );
    }
    
    /** Convert tags.
      * For instance: *italic* -> <i>italic</i>.
      * @param st the current status.
      * @param TORET the string builder for the result.
      */
    private void convertTag(TokenType st,
                            final Stack<TokenType> PENDING_TAGS,
                            final StringBuilder TORET)
    {
        boolean isClosing = false;
        
        if ( !PENDING_TAGS.isEmpty()
           && PENDING_TAGS.peek() == st )
        {
            isClosing = true;
        }
        
        // Open and close tags * -> <i> & * -> </i>
        // Find the appropriate tag
        TORET.append( this.findTag( st, isClosing ) );

        if ( isClosing ) {
            PENDING_TAGS.pop();
        } else {
            PENDING_TAGS.push( st );
        }
    }

    /** Converts markdown (Squiffy rules) to HTML.
      * - *   | _         <i>
      * - **  | __        <b>
      * - *** | ___       <b><i>
      * - `code`          <code>code</code>
      * - [stairs]      ${stairs, ex stairs}
      * - [[kitchen]](e)    ${kitchen, east}
      * @return a string containing the HTML version of the given Markdown.
      * @throws CompileError if something goes wrong.
    */
    public String convert() throws CompileError
    {
        final StringBuilder TORET = new StringBuilder( this.TEXT.length() );
        final Stack<TokenType> PENDING_TAGS = new Stack<>();
        TokenType st = TokenType.CHR;
        final Lexer LEX = new Lexer( this.TEXT );
        
        while( !LEX.isEOL() ) {
            char ch = LEX.getCurrentChar();
            st = findTokenType( st, ch );

            switch ( st ) {
                case CHR -> {
                    st = TokenType.CHR;
                    TORET.append( ch );
                }
                case REFERENCE -> {
                    st = TokenType.CHR;
                    this.convertReference( LEX, TORET );
                }
                case MOVEMENT -> {
                    st = TokenType.CHR;
                    this.convertMovement( LEX, TORET );
                }
                default -> {
                    this.convertTag( st, PENDING_TAGS, TORET );
                    st = TokenType.CHR;
                }
            }
            
            LEX.advance();
        }
        
        if ( !PENDING_TAGS.isEmpty() ) {
            throw new CompileError( "convert: pending close tag: "
                                    + PENDING_TAGS.peek() );
        }

        return TORET.toString();
    }

    /** A finite state machine.
     * @param st the current state
     * @param ch the current char
     * @return the new status, given the current status.
     */
    private TokenType findTokenType(TokenType st, char ch)
    {
        TokenType toret = TokenType.CHR;


        if ( st == TokenType.CHR ) {
            TokenType nextState = chrStates.get( ch );

            if ( nextState != null ) {
                toret = nextState;
            }
        }

        return toret;
    }

    /** Returns the corresponding HTML, i.e. <i> or its closed, </i>
     * @param st the state of the finite machine.
     * @param closed a boolean instructing to serve the open or close version.
     * @return the corresponding HTML tag, given the current status.
     */
    private String findTag(TokenType st, boolean closed)
    {
        final Map<TokenType, String> TAGS = closed ? closeTags : openTags;

        return TAGS.get( st );
    }

    /** @return the entity whose desc we are converting to HTML. */
    public Entity getEntity()
    {
        return this.ENT;
    }

    @Override
    public String toString()
    {
        try {
            return this.convert();
        } catch(CompileError exc) {
            return "";
        }
    }

    private final Entity ENT;
    private final String TEXT;

    /** The states of the finite state machine. */
    private enum TokenType {
        CHR, ITALIC, BOLD, HIGHLIGHT, CODE, REFERENCE, MOVEMENT
    }

    /** Relates the states and the open HTML tags. */
    private static Map<TokenType, String> openTags;

    /** Relates the states and the closed HTML tags. */
    private static Map<TokenType, String> closeTags;

    /** Relates the states and the read chars. */
    private static Map<Character, TokenType> chrStates;
}
