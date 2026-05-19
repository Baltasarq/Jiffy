// Jiffy (c) 2023, 2025 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;


/** Generic catalog of indexed objects.
  * @author baltasarq
  */
public class IndexedCatalog<T extends Identifiable> {
    /** Create a new, empty catalog. */
    public IndexedCatalog()
    {
        this.cat = new HashMap<>();
    }
    
    public void add(T x)
    {
        if ( x == null ) {
            throw new Error( "IndexedCatalog.add(): trying to insert a null" );
        }

        this.cat.put( x.getId(), x );
    }
    
    /** @return the object paired with a given id.
      * @param id the given id.
      */
    public T get(Id id)
    {
        return this.cat.get( id );
    }
    
    /** @return the number of elements catalogued. */
    public int count()
    {
        return this.cat.size();
    }
    
    /** @return all the catalogued elements. */
    public List<T> all()
    {
        return new ArrayList<>( this.cat.values() );
    }
    
    private final Map<Id, T> cat;
}
