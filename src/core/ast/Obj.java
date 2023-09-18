// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package core.ast;


import core.Id;


// Represents objects (inside locs).
public class Obj extends Entity {
    public Obj(Id id, Id ownerId)
    {
        super( id );
        this.OWNER = ownerId;
    }

    /** @return the id of the owner loc. */
    public Id getOwnerId()
    {
        return this.OWNER;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " /" + super.toString();
    }

    private final Id OWNER;
}
