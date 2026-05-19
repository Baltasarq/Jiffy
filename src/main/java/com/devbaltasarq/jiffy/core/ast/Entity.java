// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import com.devbaltasarq.jiffy.core.Catalog;
import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.HtmlFromMarkdown;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.parser.Vbles;
import com.devbaltasarq.jiffy.core.Identifiable;

import java.util.List;


/** Base class for Objs and Locs. */
public abstract class Entity implements Identifiable {
    private final String DESC_LINE_DELIMITER = " \\\n";

    public Entity(AST AST, String name) throws CompileError
    {
        this.AST = AST;
        name = name.trim();
        
        this.id = new Id( name );
        this.syns = new Catalog<>();
        this.desc = "";
        this.VBLES = new Vbles();
        this.setTitleFromId();
    }
    
    private void setTitleFromId()
    {
        // Put the first char in upper case for the title
        this.title = this.getId().get().toLowerCase();
        
        if ( this.title.length() > 0 ) {
            this.title = this.title.substring( 0, 1 ).toUpperCase()
                         + this.title.substring( 1 );
        }
    }

    @Override
    public Id getId()
    {
        return this.id;
    }
    
    /** @return the title of this entity: by default, the id. */
    public String getTitle()
    {
        return this.title;
    }
    
    /** Change the title of this room.
      * @param title the new title for the entity.
      */
    public void changeTitle(String title)
    {
        title = title.trim();
        
        if ( !title.isEmpty() ) {
            this.title = title.trim();
        } else {
            this.setTitleFromId();
        }
    }

    public List<String> getSyns()
    {
        return this.syns.all();
    }

    public void addSyn(String syn)
    {
        this.syns.add( syn.trim().toLowerCase() );
    }

    public void setDesc(String desc) throws CompileError
    {
        this.desc = desc;
        this.desc = new HtmlFromMarkdown( this ).convert();
        this.desc = divideInLinesWith( 70, this.desc, DESC_LINE_DELIMITER + "    " );
    }

    public String getDesc()
    {
        return desc;
    }

    public Vbles getVbles()
    {
        return this.VBLES;
    }

    /** @return the containing AST. */
    public AST getAST()
    {
        return this.AST;
    }

    @Override
    public String toString()
    {
        return this.id.get();
    }
    
    private static String divideInLinesWith(int cols, String txt, String delimiter)
    {
        final StringBuilder TORET = new StringBuilder( txt.length() );
        int pos = 0;
        int nextCol = cols;

        while( nextCol > pos
            && nextCol < txt.length() )
        {
            if ( txt.charAt( nextCol ) == ' ' ) {
                TORET.append( txt, pos, nextCol );
                TORET.append( delimiter );

                pos = nextCol;
                nextCol += cols;
            } else {
                --nextCol;
            }
        }

        TORET.append( txt.substring( pos ) );
        return TORET.toString();
    }

    private final Id id;
    private String title;
    private String desc;
    private final Vbles VBLES;
    private final AST AST;
    private final Catalog<String> syns;
}
