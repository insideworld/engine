package insideworld.engine.data.generator.inmemory.entity.search;

import insideworld.engine.data.generator.EntityInfo;
import insideworld.engine.entities.Entity;

public class InMemoryInfo implements EntityInfo {

    private final Class<? extends Entity> entity;

    private final String implementation;

    public InMemoryInfo(
        final Class<? extends Entity> entity,
        final String implementation
    ) {
        this.entity = entity;
        this.implementation = implementation;
    }

    public Class<? extends Entity> getEntity() {
        return this.entity;
    }

    public String getImplementation() {
        return this.implementation;
    }

}
