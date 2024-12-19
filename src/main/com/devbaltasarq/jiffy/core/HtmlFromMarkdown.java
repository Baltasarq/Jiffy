// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.errors.CompileError;

import java.util.HashMap;
import java.util.Map;


/** Markdown To HTML converter. */
public class HtmlFromMarkdown {
    private final char CTRL_HIGHLIGHT = '\u0011';
    private final char CTRL_BOLD = '\u0012';
    private final char CTRL_MOV = '\u0006';
    private final String REF_PATTERN = "${$APPARENT_REF, ex $REF}";
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
            final Status[] STATS = {
                    Status.ITALIC,
                    Status.BOLD,
                    Status.HIGHLIGHT,
                    Status.CODE };
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
                    CTRL_HIGHLIGHT, CTRL_BOLD, CTRL_MOV, '*', '[', ']', '`'
            };

            final Status[] STATS_FOR_CHRS = {
                    Status.HIGHLIGHT, Status.BOLD, Status.MOVEMENT,
                    Status.ITALIC, Status.REFERENCE, Status.REFERENCE, Status.CODE
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

    /** Converts markdown (Squiffy rules) to HTML.
      * - *   | _         <i>
      * - **  | __        <b>
      * - *** | ___       <b><i>
      * - `code`          <code>code</code>
      * - [escalera]      ${stairs, ex stairs}
      * - [[e, salón]]    ${salón, este}
      * @return a string containing the HTML version of the given Markdown.
    */
    public String convert() throws CompileError
    {
        final StringBuilder TORET = new StringBuilder( this.TEXT.length() );
        Status st = Status.CHR;
        Status prevSt = Status.CHR;
        int posRef = 0;

        for(char ch: this.TEXT.toCharArray()) {
            st = findStatus( st, ch );

            if ( st == Status.CHR ) {
                TORET.append( ch );
            }
            else
            if ( st == Status.REFERENCE ) {
                if ( prevSt == Status.CHR ) {
                    prevSt = st;
                    st = Status.CHR;
                    posRef = TORET.length();
                } else {
                    String refApparent = TORET.substring( posRef, TORET.length() );
                    String ref = Util.varNameFromId( "", refApparent ).toLowerCase();

                    // Remove apparition of $REF so far
                    TORET.replace( posRef, TORET.length(), "" );

                    prevSt = st = Status.CHR;

                    // Append the reference pattern: ${REF, ex REF}
                    TORET.append( REF_PATTERN.replace( "$APPARENT_REF", refApparent )
                                             .replace( "$REF", ref ) );
                }
            }
            else
            if ( st == Status.MOVEMENT ) {
                if ( prevSt == Status.CHR ) {
                    prevSt = st;
                    st = Status.CHR;
                    posRef = TORET.length();
                } else {
                    String locName = TORET.substring( posRef, TORET.length() );

                    // Remove apparition of $REF so far
                    TORET.replace( posRef, TORET.length(), "" );
                    prevSt = st = Status.CHR;

                    // Look for loc
                    //final Var VBLES = this.getEntity().getExitTo( locName );



                    // Append the reference pattern: ${REF, ex REF}
                    TORET.append( MOV_PATTERN
                            .replace( "$TEXT", locName ) );
                }
            } else {
                TORET.append( this.findTag( st, prevSt != Status.CHR ) );

                if ( prevSt == Status.CHR ) {
                    prevSt = st;
                    st = Status.CHR;
                } else {
                    prevSt = st = Status.CHR;
                }
            }
        }

        return TORET.toString();
    }

    /** A finite state machine.
     * @param st the current state
     * @param ch the current char
     * @return the new status, given the current status.
     */
    private Status findStatus(Status st, char ch)
    {
        Status toret = Status.CHR;


        if ( st == Status.CHR ) {
            Status nextState = chrStates.get( ch );

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
    private String findTag(Status st, boolean closed)
    {
        final Map<Status, String> TAGS = closed ? closeTags : openTags;

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
    private enum Status {
        CHR, ITALIC, BOLD, HIGHLIGHT, CODE, REFERENCE, MOVEMENT
    }

    /** Relates the states and the open HTML tags. */
    private static Map<Status, String> openTags;

    /** Relates the states and the closed HTML tags. */
    private static Map<Status, String> closeTags;

    /** Relates the states and the read chars. */
    private static Map<Character, Status> chrStates;
}
