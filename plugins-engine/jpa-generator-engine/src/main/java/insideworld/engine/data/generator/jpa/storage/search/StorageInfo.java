package insideworld.engine.data.generator.jpa.storage.search;

import insideworld.engine.entities.Entity;

public class StorageInfo {

    private Class<? extends Entity> entity;
    private boolean override;
    private String implementation;

    public StorageInfo(final Class<? extends Entity> entity,
                       final boolean override,
                       final String implementation) {

        this.entity = entity;
        this.override = override;
        this.implementation = implementation;
    }

    public Class<? extends Entity> getEntity() {
        return this.entity;
    }

    public boolean isOverride() {
        return this.override;
    }

    public String getImplementation() {
        return this.implementation;
    }
}
