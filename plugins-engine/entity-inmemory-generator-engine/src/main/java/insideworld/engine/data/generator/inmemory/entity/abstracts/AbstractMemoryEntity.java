package insideworld.engine.data.generator.inmemory.entity.abstracts;

import insideworld.engine.entities.Entity;

public abstract class AbstractMemoryEntity implements MemoryEntity {

    private long id;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }

    @Override
    public boolean equals(final Object obj) {
        return this.getClass().equals(obj.getClass()) && this.id == ((Entity) obj).getId();
    }
}
