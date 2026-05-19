// Jiffy (c) 2025 Baltasar MIT License <baltasarq@gmail.com>


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.ast.Loc;
import com.devbaltasarq.jiffy.core.ast.Obj;
import com.devbaltasarq.jiffy.core.parser.Vbles;
import com.devbaltasarq.jiffy.core.parser.Var;
import com.devbaltasarq.jiffy.core.Parser;
import com.devbaltasarq.jiffy.core.errors.CompileError;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public final class InputTestLongNames {
    @BeforeEach
    public void setUp()
    {
        this.title = "test";
        this.longTitle = "This is a test";
        this.author = "author1";
        this.pic = "pic1.jpg";
        this.intro = "Caminando por la vida.";
        this.version = "v1";
        this.start = "loc1";
        this.longStart = "This is Loc1";
        this.loc1Desc = "Caminas por un extraño [sendero].";
        this.loc1DescCnvt = "Caminas por un extraño ${sendero, ex sendero}.";
        this.scenery1 = "sendero";
        this.longScenery1 = "Sendero sinuoso";
        this.scenery1Desc = "El sendero está bordeado por rara maleza.";
        
        String inputText =
                """
                [[$title|$long_title]]
                @author = "$author"
                @pic = "$pic"
                @version = "$version"
                @start = "$start"
                
                $intro
                
                [[$start|$long_start]]
                
                $desc
                
                [$scenery1|$long_scenery1]
                
                $sc1desc
                ...
                """
                    .replace( "$title", this.title )
                    .replace( "$long_title", this.longTitle )
                    .replace( "$author", this.author )
                    .replace( "$pic", this.pic )
                    .replace( "$version", this.version )
                    .replace( "$start", this.start )
                    .replace( "$long_start", this.longStart )
                    .replace( "$intro", this.intro )
                    .replace( "$desc", this.loc1Desc )
                    .replace( "$scenery1", this.scenery1 )
                    .replace( "$long_scenery1", this.longScenery1 )
                    .replace( "$sc1desc", this.scenery1Desc );
        
        this.text = List.of( inputText.split( "\n" ) );

        try {
            this.compile();
        } catch(CompileError exc) {
            Assertions.fail( "Compiling: " + exc.getMessage() );
        }
    }

    @Test
    public void testStoryAttributes() throws CompileError
    {
        final Vbles VBLES = this.ast.getStory().getVbles();
        
        System.out.println( "STORY title: '"
                                + this.ast.getStory().getTitle()
                                + "'"
        );
        
        System.out.println( "STORY desc: '"
                                + this.ast.getStory().getDesc()
                                + "'"
        );
        
        System.out.println( "VBLES size: " + VBLES.count() );
        final List<Var> VARS = VBLES.all();
        int i = 0;
        for(final Var VAR: VARS) {
            i += 1;
            
            if ( VAR == null ) {
                System.out.println( i + ". null VAR" );
                continue;
            }
            
            System.out.println(
                    String.format( "%d. '%s': %s",
                            i,
                            VAR.getId(),
                            VAR.getRValue() ) );
        }
        
        assertEquals(
                this.author,
                this.ast.getStory()
                        .getVbles()
                        .get( new Id( "author" ) )
                        .getRValue().toString() );
        
        assertEquals(
                this.pic,
                this.ast.getStory()
                        .getVbles()
                        .get( new Id( "pic" ) )
                        .getRValue().toString() );
        
        assertEquals(
                this.start,
                this.ast.getStory()
                        .getVbles()
                        .get( new Id( "start" ) )
                        .getRValue().toString() );
        
        assertEquals(
                this.version,
                this.ast.getStory()
                        .getVbles()
                        .get( new Id( "version" ) )
                        .getRValue().toString() );

        assertEquals(
                this.intro,
                this.ast.getStory().getDesc() );
        
        assertEquals(
                this.longTitle,
                this.ast.getStory().getTitle() );
        
        assertEquals(
                this.title,
                this.ast.getStory().getId().get() );
    }
    
    @Test
    public void testLocs()
    {
        final List<Loc> LOCS = this.ast.getStory().getLocs();
        final List<Loc> GEN_LOCS = this.ast.getStory().getLocs();
        
        showLocs( "Story", LOCS );
        showLocs( "Gen", GEN_LOCS );
        
        assertEquals( LOCS, GEN_LOCS );
        assertEquals( 1, LOCS.size() );
        
        final Loc LOC = LOCS.get( 0 );
        final List<Obj> OBJS = LOC.getObjs();
        
        assertEquals( this.start, LOC.getId().get() );
        assertEquals( this.loc1DescCnvt, LOC.getDesc() );
        assertEquals( 1, OBJS.size() );
        
        final Obj OBJ = OBJS.get( 0 );
        
        assertEquals( this.scenery1, OBJ.getId().toString() );
        assertEquals( this.scenery1Desc, OBJ.getDesc() );
    }
    
    private void compile() throws CompileError
    {
        final Parser PARSE = new Parser();

        this.ast = PARSE.getAST();
        
        for(String line: this.text) {
            PARSE.parse( line );
        }        
    }
    
    private static void showLocs(String id, final List<Loc> LOCS)
    {
        System.out.println( id + " locs: " + LOCS.size() );
        for(final Loc LOC: LOCS) {
            System.out.println(
                        String.format(
                            "Loc %s('%s'): '%s'",
                            LOC.getId(),
                            LOC.getTitle(),
                            LOC.getDesc() ));
        }
    }
    
    private List<String> text;
    private AST ast;
    private String title;
    private String longTitle;
    private String author;
    private String pic;
    private String intro;
    private String version;
    private String start;
    private String longStart;
    private String loc1Desc;
    private String loc1DescCnvt;
    private String scenery1Desc;
    private String scenery1;
    private String longScenery1;
}
