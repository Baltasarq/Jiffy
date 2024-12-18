// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core.ast;


import com.baltasarq.jiffy.core.AST;
import com.baltasarq.jiffy.core.parser.Var;

import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class JsonWriter {
    public JsonWriter(AST ast)
    {
        this.AST = ast;
    }

    /** Writes the target code to a file.
      * @param f a file object to write to.
      */
    public void write(File f) throws IOException
    {
        try (Writer wrf = new PrintWriter( f, StandardCharsets.UTF_8 )) {
            this.write( wrf );
        } catch(UnsupportedEncodingException exc)
        {
            throw new Error( "unsupported encoding?: " + exc.getMessage() );
        }
    }

    public void write(Writer f)
    {
        final JSONObject JSON_STORY = new JSONObject();

        // Special story fields
        JSON_STORY.put( "ifid", this.AST.getStory().getIfId() );

        // Recursively add the story
        this.addEntity( JSON_STORY,
                        this.AST.getStory().getId().get(),
                        this.AST.getStory() );


        JSON_STORY.write( f, 4, 4 );
    }

    private void addEntity(JSONObject jobj, String key, Entity ent)
    {
        // Create sub-object
        final JSONObject SUB_OBJ = new JSONObject();

        // Add it to the parent object
        jobj.put( key, SUB_OBJ );

        // Set the main attributes
        SUB_OBJ.put( "desc", ent.getDesc() );

        // Set the variables
        for(Var vble: ent.getVbles().getAll()) {
            SUB_OBJ.put( vble.getId().get(), vble.getRValue().toString() );
        }

        // Set the locs, if needed
        if ( ent instanceof final Story STORY ) {
            for(Loc loc: STORY.getLocs()) {
                this.addEntity( SUB_OBJ, loc.getId().get(), loc );
            }
        }
        else
        // Set the objects, if needed
        if ( ent instanceof Loc loc ) {
            for(Obj obj: loc.getObjs()) {
                this.addEntity( SUB_OBJ, obj.getId().get(), obj );
            }
        }
    }

    private final AST AST;
}
