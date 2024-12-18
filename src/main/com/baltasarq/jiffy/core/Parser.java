// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core;


import com.baltasarq.jiffy.core.ast.Entity;
import com.baltasarq.jiffy.core.ast.Loc;
import com.baltasarq.jiffy.core.ast.Obj;
import com.baltasarq.jiffy.core.parser.RValue;
import com.baltasarq.jiffy.core.errors.CompileError;
import com.baltasarq.jiffy.core.parser.Var;
import com.baltasarq.jiffy.core.parser.literals.BoolLiteral;
import com.baltasarq.jiffy.core.parser.literals.IntLiteral;
import com.baltasarq.jiffy.core.parser.literals.RealLiteral;
import com.baltasarq.jiffy.core.parser.literals.StrLiteral;

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
        this.DESC = new StringBuilder( 160 );
    }

    public AST parseFile(String fileName) throws CompileError
    {
        try (InputStream in = Files.newInputStream( Paths.get( fileName ) );
             BufferedReader reader =
                     new BufferedReader( new InputStreamReader( in ) ) )
        {
            String line = "";

            while ( ( line = reader.readLine() ) != null ) {
                ++numLine;
                line = line.trim();

                if ( !line.isEmpty()
                  && line.charAt( 0 ) != Lexer.COMMENT )
                {
                    this.parse( line );
                }
            }
        } catch (IOException x) {
            throw new CompileError( x.getMessage() );
        }

        return this.AST;
    }

    public void parse(String line) throws CompileError
    {
        this.lex = new Lexer( line );

        if ( this.lex.match( Lexer.OPENED_SQ_BRACKET ) ) {
            this.lex.advance( -1 );
            this.storeCurrentDesc();
            this.popState();
            final Entity ENT = this.parseEntity();

            if ( ENT instanceof Loc ) {
                if ( !this.AST.getStory().getId().equals( Id.empty() ) )
                {
                    this.AST.add( ENT );
                } else {
                    this.AST.getStory().setId( ENT.getId() );
                }

                this.state = Status.LOC;
            } else {
                final Entity PARENT = this.AST.current( this.state );

                if ( PARENT instanceof final Loc LOC ) {
                    LOC.add( (Obj) ENT );
                    this.state = Status.OBJ;
                } else {
                    throw new Error( "unexpected: not a loc: "
                                      + PARENT.toString()
                                      + " to add obj: "
                                      + ENT.toString() );
                }
            }
        }
        else
        if ( this.lex.match( Lexer.VAR ) ) {
            this.lex.advance( -1 );
            final Var VAR = this.parseVariable();

            if ( !this.AST.getLocs().isEmpty()
              && ( this.state == Status.LOC
                || this.state == Status.OBJ ) )
            {
                this.AST.current( this.state ).getVbles().add( VAR );
            }
            else
            if ( this.AST.getLocs().isEmpty()
              && this.state == Status.LOC )
            {
                // We are parsing the story info as a loc
                if ( this.AST.IF_VBLES.contains( VAR.getId().get() ) ) {
                    this.AST.getStory().getVbles().add( VAR );
                } else {
                    throw new CompileError( this.numLine,
                                            this.lex.getPos(),
                                            "vble cannot be set for whole story: "
                                                    + VAR.getId().get() );
                }
            } else {
                throw buildError( "unexpected error parsing variable" );
            }
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
                if ( this.state == Status.STORY ) {
                    this.AST.getStory().setDesc( STR_DESC );
                }

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
            this.lex.advance( - Lexer.OPEN_LOC.length() );
            toret = this.parseLoc();
        }
        else
        if ( this.lex.match( Lexer.OPENED_SQ_BRACKET ) ) {
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
        } else {
            throw buildError( "expected " + Lexer.OPEN_LOC );
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

        if ( nextTokenType == Lexer.TokenType.BOOL ) {
            toret = new BoolLiteral( Boolean.parseBoolean( lex.getToken() ) );
        }
        else
        if ( nextTokenType == Lexer.TokenType.STR ) {
            toret = new StrLiteral( lex.getStringLiteral() );
        }
        else
        if ( nextTokenType == Lexer.TokenType.NUMBER ) {
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
        } else {
            throw buildError( "literal expected" );
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

    private final String DESC_FIRST_ALLOWED_CHARS = ".-_\"'¡¿";
    private final StringBuilder DESC;
    private int numLine;
    private Lexer lex;
    private Status state;
    private final AST AST;
}
