// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package tests;


import core.errors.CompileError;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;

import core.HtmlFromMarkdown;


class HtmlFromMarkdownTest {
    @BeforeEach
    void setUp()
    {
        this.cnvtEmpty = new HtmlFromMarkdown( "" );
        this.cnvtSpaces = new HtmlFromMarkdown( "    " );
        this.cnvtMarks = new HtmlFromMarkdown( "`Esto` ***es*** **una** *prueba*" );
        this.cnvtRef = new HtmlFromMarkdown( "[puerta] a la [calle]" );
        this.cnvtRefMays = new HtmlFromMarkdown( "[Puerta] a la [calle]" );
        this.cnvtRefAccents = new HtmlFromMarkdown( "[Cómoda] y [fácil] cajonera." );
        this.cnvtMov = new HtmlFromMarkdown( "[[e, Cocina]] y [[o, Baño]] son salidas posibles." );
        this.cnvtMovErr = new HtmlFromMarkdown( "[[Cocina]]" );
    }

    @Test
    void convertEmpty() throws CompileError
    {
        Assertions.assertEquals( "", this.cnvtEmpty.convert() );
    }

    @Test
    void convertSpaces() throws CompileError
    {
        Assertions.assertEquals( "", this.cnvtSpaces.convert() );
    }

    @Test
    void convertCommonMarks() throws CompileError
    {
        Assertions.assertEquals( "<code>Esto</code> <b><i>es</i></b> <b>una</b> <i>prueba</i>",
                                  this.cnvtMarks.convert() );
    }

    @Test
    void convertRegularRefs() throws CompileError
    {
        Assertions.assertEquals( "${puerta, ex puerta} a la ${calle, ex calle}",
                                    this.cnvtRef.convert() );
    }

    @Test
    void convertMaysRefs() throws CompileError
    {
        Assertions.assertEquals( "${Puerta, ex puerta} a la ${calle, ex calle}",
                                    this.cnvtRefMays.convert() );
    }

    @Test
    void convertAccentedRefs() throws CompileError
    {
        Assertions.assertEquals( "${Cómoda, ex comoda} y ${fácil, ex facil} cajonera.",
                this.cnvtRefAccents.convert() );
    }

    @Test
    void convertMovement() throws CompileError
    {
        Assertions.assertEquals( "${Cocina, e} y ${Baño, o} son salidas posibles.",
                this.cnvtMov.convert() );
    }

    @Test
    void convertMovementErr()
    {
        Assertions.assertThrows( CompileError.class, () -> {
            Assertions.assertEquals( "${Cocina, e}.", this.cnvtMovErr.convert() );
        });
    }

    HtmlFromMarkdown cnvtEmpty;
    HtmlFromMarkdown cnvtSpaces;
    HtmlFromMarkdown cnvtMarks;
    HtmlFromMarkdown cnvtRef;
    HtmlFromMarkdown cnvtRefMays;
    HtmlFromMarkdown cnvtRefAccents;
    HtmlFromMarkdown cnvtMov;
    HtmlFromMarkdown cnvtMovErr;
}