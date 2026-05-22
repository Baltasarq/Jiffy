// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.parser.Var;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


public class TrizbortWriter {
    public TrizbortWriter(AST ast)
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
        final JSONArray ELEMENTS = new JSONArray();
        final Function<String, String> strFromDesc =
                                    (desc) -> desc.replace( "\n", "" )
                                            .replace( "\\", "" )
                                            .replace( "    ", "" );
        final String DESC = strFromDesc.apply(this.AST.getStory().getDesc() );
        final var LOCS = this.AST.getStory().getLocs();
        int connectorId = LOCS.size() + 1000;
        
        // Prepare loc ids
        final var LOC_IDS = new HashMap<Id, Integer>();
        int numId = 0;
        
        for (Loc loc: LOCS) {
            LOC_IDS.put( loc.getId(), numId++ );
        }
        
        // Special story fields
        JSON_STORY.put( "title", this.AST.getStory().getTitle() );
        JSON_STORY.put( "description", DESC );
        JSON_STORY.put( "startRoom", 0 );
        
        // Add all locs
        for(int i = 0; i < LOCS.size(); ++i) {
            final Loc LOC = LOCS.get( i );
            
            ELEMENTS.put( jobjFromLoc( i, LOC, strFromDesc ) );
            
            final List<Direction> ALL_DIR_EXITS = LOC.getAllExitDirections();
            for(int j = 0; j < ALL_DIR_EXITS.size(); ++j) {
                final Direction DIR = ALL_DIR_EXITS.get( j );
                final Id TARGET_ID = LOC.getExitAt( DIR );
                final Loc TARGET_LOC = this.AST.getStory().getLoc( TARGET_ID );
                
                if ( TARGET_LOC != null ) {
                    ELEMENTS.put( jobjFromExit(
                                                connectorId++,
                                                LOC_IDS,
                                                DIR,
                                                LOC,
                                                TARGET_LOC ));
                }
            }
        }

        JSON_STORY.put( "elements", ELEMENTS );
        JSON_STORY.write( f, 4, 4 );
    }
    
    private JSONObject jobjFromExit(
                                int i,
                                Map<Id, Integer> locIds,
                                Direction dir,
                                Loc orgLoc,
                                Loc destLoc)
    {
        final JSONObject TORET = new JSONObject();
        Integer locOrgIdInt = locIds.get( orgLoc.getId() );
        Integer locDestIdInt = locIds.get( destLoc.getId() );
        int locOrgId = locOrgIdInt != null ? locOrgIdInt : -1;
        int locDestId = locDestIdInt != null ? locDestIdInt : -1;
        
        TORET.put( "id", i );
        TORET.put( "_guid", UUID.randomUUID() );
        TORET.put( "_type", "Connector" );
        TORET.put( "_name", "" );
        TORET.put( "_dockStart", locOrgId );
        TORET.put( "_dockEnd", locDestId );
        TORET.put( "_lineStyle", 1 );
        
        return TORET;
    }
    
    private JSONObject jobjFromLoc(int i, Loc loc, Function<String, String> strFromDesc)
    {
        final JSONObject TORET = new JSONObject();
        final String DESC = strFromDesc.apply( loc.getDesc() );
        
        TORET.put( "id", i );
        TORET.put( "_guid", loc.getId() );
        TORET.put( "_x", i * 75 );
        TORET.put( "_y", i * 75 );
        TORET.put( "_type", "Room" );
        TORET.put( "_name", loc.getTitle() );
        TORET.put( "_subtitle", "" );
        TORET.put( "_description", DESC );
        TORET.put( "_dark", false );
        TORET.put( "_endroom", false );
        TORET.put( "_objects", new JSONArray() );
        TORET.put( "_lineStyle", 1 );
        TORET.put( "_shape", 0 );
        
        return TORET;
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
        for(Var vble: ent.getVbles().all()) {
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
