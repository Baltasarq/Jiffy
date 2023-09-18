// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.ast;


import core.Id;
import core.errors.CompileError;


import java.util.*;


public class Story extends Loc {
    public Story(Id id)
    {
        super( id );
        this.LOCS = new HashMap<>();
        this.current = this;
        this.ifId = UUID.randomUUID();
    }

    /** @return the if id of this story, a class 4 (random) uuid. */
    public UUID getIfId()
    {
        return this.ifId;
    }

    public void setId(Id id)
    {
        this.id = id;
    }

    public void add(Loc loc) throws CompileError
    {
        if ( loc == null ) {
            throw new Error( "Story.add(): trying to insert a null LOC" );
        }

        final Id ID = loc.getId();

        if ( this.LOCS.get( ID ) != null ) {
            throw new CompileError( "object '"
                    + ID
                    + "' already exists in: " + this.getId() );
        }

        this.LOCS.put( loc.getId(), loc );
        this.current = loc;
    }

    public Loc getLocs(Id id)
    {
        return this.LOCS.get( id );
    }

    public Loc currentLoc()
    {
        return this.current;
    }

    public List<Loc> getLocs()
    {
        return new ArrayList<>( this.LOCS.values() );
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " /" + super.toString();
    }

    private Loc current;
    private final UUID ifId;
    private final Map<Id, Loc> LOCS;
}
