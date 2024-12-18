// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.baltasarq.jiffy.core;


import com.baltasarq.jiffy.core.errors.CompileError;


/** Represents the id's for entities. */
public class Id {
    public static final String EMPTY_STORY_ID = "|";

    public Id(String strId) throws CompileError
    {
        if ( strId == null ) {
            throw new Error( "trying to build Id with a null" );
        }

        strId = strId.trim();

        if ( strId.isEmpty() ) {
            throw new CompileError( "trying to create an empty id" );
        }

        this.id = idFromTxt( strId );
    }

    public String get()
    {
        return this.id;
    }

    public int hashCode()
    {
        return this.id.hashCode();
    }

    public boolean equals(Object other)
    {
        boolean toret = false;

        if ( other instanceof Id ) {
            toret = this.id.equals( ( (Id) other ).get() );
        }

        return toret;
    }

    @Override
    public String toString()
    {
        return this.get();
    }

    private static String idFromTxt(String txt)
    {
        return Util.varNameFromId( "", txt.trim() ).toLowerCase();
    }

    public static Id empty()
    {
        if ( emptyId == null ) {
            try {
                emptyId = new Id( EMPTY_STORY_ID );
            } catch(CompileError exc) {
                throw new Error( "creating empty_id" );
            }
        }

        return emptyId;
    }

    private static Id emptyId;
    private final String id;
}
