// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.ast;


import core.AST;
import core.Util;
import core.HtmlFromMarkdown;
import core.Id;
import core.errors.CompileError;
import core.parser.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        this.VBLES = new HashMap<>();
    }

    public Id getId()
    {
        return this.id;
    }

    public List<String> getySyns()
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

    public void add(Var vble) throws CompileError
    {
        if ( vble == null ) {
            throw new Error( "AST.add(): trying to insert a null OBJ" );
        }

        final Id ID = vble.getId();

        if ( this.VBLES.get( ID ) != null ) {
            throw new CompileError( "variable '"
                                    + ID
                                    + "' already exists in: " + this.id );
        }

        this.VBLES.put( vble.getId(), vble );
    }

    public Var getVble(Id id)
    {
        return this.VBLES.get( id );
    }

    public List<Var> getVbles()
    {
        return new ArrayList<>( this.VBLES.values() );
    }

    /** @return the containing AST. */
    private AST getAST()
    {
        return this.AST;
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
    private final Map<Id, Var> VBLES;
}
