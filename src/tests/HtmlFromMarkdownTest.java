// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package tests;


import core.AST;
import core.Id;
import core.ast.Loc;
import core.errors.CompileError;
import core.parser.Var;
import core.parser.literals.StrLiteral;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;


class HtmlFromMarkdownTest {
    @BeforeEach
    void setUp() throws CompileError
    {
        this.ast = new AST();
        this.loc = new Loc( this.ast, "locDemo" );
    }

    @Test
    void convertEmpty() throws CompileError
    {
        this.loc.setDesc( "" );
        Assertions.assertEquals( "", this.loc.getDesc() );
    }

    @Test
    void convertSpaces() throws CompileError
    {
        this.loc.setDesc( "    " );
        Assertions.assertEquals( "", this.loc.getDesc() );
    }

    @Test
    void convertCommonMarks() throws CompileError
    {
        this.loc.setDesc( "`Esto` ***es*** **una** *prueba*" );
        Assertions.assertEquals( "<code>Esto</code> <b><i>es</i></b> <b>una</b> <i>prueba</i>",
                                  this.loc.getDesc() );
    }

    @Test
    void convertRegularRefs() throws CompileError
    {
        this.loc.setDesc( "[puerta] a la [calle]" );
        Assertions.assertEquals( "${puerta, ex puerta} a la ${calle, ex calle}",
                                  this.loc.getDesc() );
    }

    @Test
    void convertMaysRefs() throws CompileError
    {
        this.loc.setDesc( "[Puerta] a la [calle]" );
        Assertions.assertEquals( "${Puerta, ex puerta} a la ${calle, ex calle}",
                                  this.loc.getDesc() );
    }

    @Test
    void convertAccentedRefs() throws CompileError
    {
        this.loc.setDesc( "[Cómoda] y [fácil] cajonera." );
        Assertions.assertEquals( "${Cómoda, ex comoda} y ${fácil, ex facil} cajonera.",
                                  this.loc.getDesc() );
    }

    @Test
    void convertMovement() throws CompileError
    {
        Loc locCocina = new Loc( this.ast, "Cocina" );
        Loc locBanno = new Loc( this.ast, "Baño" );

        // Prepare
        this.loc.add( new Var( new Id( "con_east" ), new StrLiteral( "Cocina" )) );
        this.loc.add( new Var( new Id( "con_west" ), new StrLiteral( "Baño" )) );

        // Chk
        this.loc.setDesc( "[[Cocina]] y [[Baño]] son salidas posibles." );
        Assertions.assertEquals( "${Cocina, e} y ${Baño, o} son salidas posibles.",
                                  this.loc.getDesc() );
    }

    private AST ast;
    private Loc loc;
}
