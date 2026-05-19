// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core;


import java.util.List;
import java.util.ArrayList;


/** A generic catalog, a list of objects.
  * @author baltasarq
  */
public class Catalog<T> {
    /** Create a new, empty catalog. */
    public Catalog()
    {
        this.cat = new ArrayList<>();
    }
    
    public void add(T x)
    {
        this.cat.add( x );
    }
    
    /** @return the object at the given position.
      * @param pos the given position.
      */
    public T at(int pos)
    {
        return this.cat.get( pos );
    }
    
    /** @return all the elements in the catalog. */
    public List<T> all()
    {
        return new ArrayList<>( this.cat );
    }
    
    /** @return the number of elements. */
    public int count()
    {
        return this.cat.size();
    }
    
    private final List<T> cat;
}
