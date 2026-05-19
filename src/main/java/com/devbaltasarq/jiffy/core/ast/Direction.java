// Jiffy (c) 2025 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import java.util.Set;
import java.util.List;


/** A compass direction.
  * @author baltasarq
  */
public enum Direction {
    NORTH( Set.of( "n", "norte", "north" ) ),
    SOUTH( Set.of( "s", "sur", "south" ) ),
    EAST( Set.of( "e", "este", "east" ) ),
    WEST( Set.of( "w", "o", "oeste", "west" ) ),
    UP( Set.of( "u", "up", "arriba", "subir", "sube" ) ),
    DOWN( Set.of( "d", "down", "abajo", "bajar", "baja" ) );
    
    Direction(Set<String> strDirs)
    {
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
    
    private final Set<String> dirSyns;
}
