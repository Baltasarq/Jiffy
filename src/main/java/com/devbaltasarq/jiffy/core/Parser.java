// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import com.devbaltasarq.jiffy.core.ast.Entity;
import com.devbaltasarq.jiffy.core.ast.Loc;
import com.devbaltasarq.jiffy.core.ast.Obj;
import com.devbaltasarq.jiffy.core.parser.RValue;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.parser.Var;
import com.devbaltasarq.jiffy.core.parser.literals.BoolLiteral;
import com.devbaltasarq.jiffy.core.parser.literals.IntLiteral;
import com.devbaltasarq.jiffy.core.parser.literals.RealLiteral;
import com.devbaltasarq.jiffy.core.parser.literals.StrLiteral;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Parser {
    public enum Status { STORY, LOC, OBJ };

    public Parser() throws CompileError
    {
        this.AST = new AST();
        this.state = Status.STORY;
        this.numLine = 0;
        this.endInput = false;
        this.DESC = new StringBuilder( 160 );
    }
    
    /** @return the compiled AST. */
    public AST getAST()
    {
        return this.AST;
    }

    /** Parsers a whole file.
      * @param fileName the file path to compile.
      * @return the AST obtained.
      * @throws CompileError if something goes wrong.
      */
    public AST parseFile(String fileName) throws CompileError
    {
        try (InputStream in = Files.newInputStream( Paths.get( fileName ) );
             BufferedReader reader =
                     new BufferedReader( new InputStreamReader( in ) ) )
        {
            String line;

            while ( ( line = reader.readLine() ) != null ) {
                ++numLine;
                line = line.trim();

                if ( !line.isEmpty()
                  && line.charAt( 0 ) != Lexer.COMMENT )
                {
                    this.parse( line );
                }
            }
            
            if ( !this.endInput ) {
                throw new CompileError( "missing end of input: \"...\"" );
            }
        } catch (IOException x) {
            throw new CompileError( x.getMessage() );
        }

        return this.AST;
    }

    /** Parses an individual text line.
      * @param line the line of text to compile.
      * @throws CompileError if something goes wrong.
      */
    public void parse(String line) throws CompileError
    {
        this.lex = new Lexer( line );

        if ( this.lex.match( Lexer.OPENED_SQ_BRACKET ) ) {
            // Parse the beginning of a Loc
            this.lex.advance( -1 );
            this.storeCurrentDesc();
            this.popState();
            final Entity ENT = this.parseEntity();

            if ( ENT instanceof final Loc LOC ) {
                if ( !this.AST.getStory().getId().equals( Id.empty() ) )
                {
                    this.AST.add( ENT );
                } else {
                    this.AST.changeStoryFor( LOC );
                }

                this.state = Status.LOC;
            } else {
                final Entity PARENT = this.AST.current( this.state );

                if ( PARENT instanceof final Loc LOC ) {
                    LOC.add( (Obj) ENT );
                    this.state = Status.OBJ;
                } else {
                    throw new Error( "unexpected: not a loc: "
                                      + PARENT
                                      + " to add obj: "
                                      + ENT );
                }
            }
        }
        else
        if ( this.lex.match( Lexer.VAR ) ) {
            // Parse a variable
            this.lex.advance( -1 );
            final Var VAR = this.parseVariable();

            if ( !this.AST.getStory().getLocs().isEmpty()
              && ( this.state == Status.LOC
                || this.state == Status.OBJ ) )
            {
                this.AST.current( this.state ).getVbles().add( VAR );
System.out.println( "VAR: '" + VAR + "' added to " + this.AST.current(state));
            }
            else
            if ( this.AST.getStory().getLocs().isEmpty()
              && this.state == Status.LOC )
            {
                // We are parsing the story info as a loc
                if ( this.AST.IF_VBLES.contains( VAR.getId().get() ) ) {
                    this.AST.getStory().getVbles().add( VAR );
System.out.println( "VAR: '" + VAR + "' added to main story.");
                } else {
                    throw new CompileError( this.numLine,
                                            this.lex.getPos(),
                                            "vble cannot be set for whole story: "
                                                    + VAR.getId().get() );
                }
            } else {
                throw buildError( "unexpected error parsing variable" );
            }
        }
        else
        if ( this.lex.match( "..." ) ) {
            // This is the end
            this.endInput = true;
            this.storeCurrentDesc();
        } else {
            // Plain text -- it's a desc
            final char FIRST_CHAR = this.lex.getCurrentChar();

            if ( Character.isDigit( FIRST_CHAR )
              || Character.isAlphabetic( FIRST_CHAR )
              || DESC_FIRST_ALLOWED_CHARS.indexOf( FIRST_CHAR ) > -1 )
            {
                if ( !this.DESC.isEmpty() ) {
                    this.DESC.append( ' ' );
                }

                this.DESC.append( line );
            }
        }

        return;
    }

    private Status popState()
    {
        if ( this.lex.match( Lexer.OPEN_LOC ) ) {
            this.state = Status.STORY;
            this.lex.advance( -Lexer.OPEN_LOC.length() );
        } else {
            this.state = Status.LOC;
        }

        return this.state;
    }

    private void storeCurrentDesc() throws CompileError
    {
        final String STR_DESC = this.DESC.toString().trim();

        if ( !STR_DESC.isEmpty() ) {
            // Look for the current entity and set the desc
            final Entity ENT = this.AST.current( this.state );

            try {
                ENT.setDesc( STR_DESC );
            } catch(CompileError exc) {
                throw buildError( exc.getMessage() );
            }

            // Clear the string builder for a new description
            this.DESC.setLength( 0 );
        }

        return;
    }

    private Entity parseEntity() throws CompileError
    {
        Entity toret = null;

        if ( this.lex.match( Lexer.OPEN_LOC ) ) {
            // Parsing a loc
            this.lex.advance( - Lexer.OPEN_LOC.length() );
            toret = this.parseLoc();
        }
        else
        if ( this.lex.match( Lexer.OPENED_SQ_BRACKET ) ) {
            // Parsing an object
            this.lex.advance( -1 );
            toret = this.parseObj( (Loc) this.AST.current( this.state ) );
        } else {
            throw new CompileError( this.numLine, "expected OBJ or LOC" );
        }

        return toret;
    }

    /** Parses an object: [id]\n@vble x = 9\ndesc */
    private Obj parseObj(final Loc OWNER) throws CompileError
    {
        Obj toret = null;

        if ( this.lex.match( Lexer.OPENED_SQ_BRACKET ) ) {
            String strId = this.lex.getToken();
            String strLongName = this.parseLongName();

            if ( strId.isEmpty() ) {
                throw buildError( "expected obj's id" );
            }

            if ( !this.lex.match( Lexer.CLOSED_SQ_BRACKET ) ) {
                throw buildError( "expected " + Lexer.CLOSED_SQ_BRACKET );
            }

            this.lex.skipSpaces();
            if ( !this.lex.isEOL() ) {
                throw buildError( "expected end of line" );
            }

            toret = new Obj( this.AST, strId, OWNER );
            
            if ( !strLongName.isEmpty() ) {
                toret.changeTitle( strLongName );
            }
        } else {
            throw buildError( "expected " + Lexer.OPENED_SQ_BRACKET );
        }

        return toret;
    }

    private Loc parseLoc() throws CompileError
    {
        Loc toret = null;

        if ( this.lex.match( Lexer.OPEN_LOC ) ) {
            String strId = this.lex.getToken();
            String strLongName = this.parseLongName();

            if ( strId.isEmpty() ) {
                throw buildError( "expected loc's id" );
            }

            if ( !this.lex.match( Lexer.CLOSE_LOC ) ) {
                throw buildError( "expected " + Lexer.CLOSE_LOC );
            }

            this.lex.skipSpaces();
            if ( !this.lex.isEOL() ) {
                throw buildError( "expected end of line" );
            }

            toret = new Loc( this.AST, strId );
            
            if ( !strLongName.isEmpty() ) {
                toret.changeTitle( strLongName );
            }
        } else {
            throw buildError( "expected " + Lexer.OPEN_LOC );
        }

        return toret;
    }
    
    private String parseLongName()
    {
        String toret = "";
        
        this.lex.skipSpaces();
        
        if ( lex.getCurrentChar() == '|' ) {
            toret = this.lex.getLiteral( '|', ']' ).trim();
            this.lex.advance( -1 );
        }
        
        return toret;
    }

    private Var parseVariable() throws CompileError
    {
        Var toret = null;

        if ( this.lex.match( Lexer.VAR ) ) {
            String id = this.lex.getToken();

            if ( this.lex.match( Lexer.ASSIGN ) ) {
                toret = new Var( new Id( id ), this.parseRValue() );
System.out.println("Parsed '" + toret.getId() + "' to be " + toret.getRValue());
            }
        } else {
            throw buildError( "expected variable id" );
        }

        return toret;
    }

    private RValue parseRValue() throws CompileError
    {
        return parseLiteral( this.lex );
    }

    private RValue parseLiteral(Lexer lex) throws CompileError
    {
        RValue toret = null;
        Lexer.TokenType nextTokenType = lex.getNextTokenType();

        lex.skipSpaces();

        switch( nextTokenType ) {
            case BOOL -> {
                toret = new BoolLiteral( Boolean.parseBoolean( lex.getToken() ) );
            }
            case STR -> {
                toret = new StrLiteral( lex.getStringLiteral() );
            }
            case NUMBER -> {
                String strNum = lex.getNumberLiteral();

                try {
                    if ( strNum.contains( "." ) ) {
                        toret = new RealLiteral( Double.parseDouble( strNum ) );
                    } else {
                        toret = new IntLiteral( Long.parseLong( strNum ) );
                    }
                }
                catch(NumberFormatException exc) {
                    toret = new IntLiteral( -1  );
                }
            }
            default -> throw buildError( "literal expected" );
        }

        return toret;
    }

    private CompileError buildError(String msg)
    {
        return new CompileError(
                        this.numLine,
                        this.lex.getPos(),
                        msg
                        + ", stopped at: "
                        + this.lex.getCurrentChar() );
    }

    private final String DESC_FIRST_ALLOWED_CHARS = ".-_\"'¡¿^{";
    private final StringBuilder DESC;
    private boolean endInput;
    private int numLine;
    private Lexer lex;
    private Status state;
    private final AST AST;
}
