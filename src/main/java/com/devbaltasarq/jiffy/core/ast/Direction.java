// Jiffy (c) 2025 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import java.util.Set;
import java.util.List;


/** A compass direction.
  * @author baltasarq
  */
public enum Direction {
    NORTH( "n", Set.of( "n", "norte", "north" ) ),
    SOUTH( "s", Set.of( "s", "sur", "south" ) ),
    EAST( "e", Set.of( "e", "este", "east" ) ),
    WEST( "o", Set.of( "w", "o", "oeste", "west" ) ),
    UP( "arriba", Set.of( "u", "up", "arriba", "subir", "sube" ) ),
    DOWN( "abajo", Set.of( "d", "down", "abajo", "bajar", "baja" ) );
    
    Direction(String name, Set<String> strDirs)
    {
        this.name = name;
        this.dirSyns = strDirs;
    }
    
    /** @return the synonyms for this direction. */
    public List<String> getDirSyns()
    {
        return this.dirSyns.stream().toList();
    }
    
    /** Looks for the actual direction from a given string.
      * @param strDir a string like "up", or "west".
      * @return a direction like Direction.UP or Direction.WEST if found,
      *         null otherwise.
      */
    public static Direction of(String strDir)
    {
        Direction toret = null; 
        
        strDir = strDir.trim().toLowerCase();
        for(Direction dir: Direction.values()) {
            if ( dir.dirSyns.contains( strDir ) ) {
                toret = dir;
                break;
            }
        }
        
        return toret;
    }
    
    @Override
    public String toString()
    {
        return this.name;
    }
    
    private final String name;
    private final Set<String> dirSyns;
}
