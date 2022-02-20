package insideworld.engine.data.generator.inmemory.entity.abstracts;

public abstract class AbstractMemoryEntity implements MemoryEntity {

    private long id;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
