// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.core.ast;


import com.devbaltasarq.jiffy.core.AST;
import com.devbaltasarq.jiffy.core.Util;
import com.devbaltasarq.jiffy.core.Id;
import com.devbaltasarq.jiffy.core.HtmlFromMarkdown;
import com.devbaltasarq.jiffy.core.errors.CompileError;
import com.devbaltasarq.jiffy.core.parser.Vbles;

import java.util.ArrayList;
import java.util.List;


public abstract class Entity {
    private final String DESC_LINE_DELIMITER = " \\\n";

    public Entity(AST AST, String name) throws CompileError
    {
        this.AST = AST;
        name = name.trim();

        this.id = new Id( name );
        this.syns = new ArrayList<>();

        if ( !this.id.equals( name ) ) {
            this.addSyn( name );
        }

        this.desc = "";
        this.VBLES = new Vbles();
    }

    public Id getId()
    {
        return this.id;
    }

    public List<String> getSyns()
    {
        return new ArrayList<>( this.syns );
    }

    public void addSyn(String syn)
    {
        this.syns.add( syn.trim().toLowerCase() );
    }

    public void setDesc(String desc) throws CompileError
    {
        this.desc = desc;
        this.desc = new HtmlFromMarkdown( this ).convert();
        this.desc = Util.divideInLinesWith( 70, this.desc, DESC_LINE_DELIMITER + "    " );
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

    public String getExitTo(String locName)
    {
      /*  final List<Var> VBLES = this.getVbles();

        for(Var VBLE: VBLES) {
            if ( VBLE.getId().get().startsWith( EXIT_PREFIX ) )
            {

            }
        }

       */
        return "";
    }

    @Override
    public String toString()
    {
        return this.id.get();
    }

    private final AST AST;
    private String desc;
    private ArrayList<String> syns;
    protected Id id;
    private Vbles VBLES;
}
