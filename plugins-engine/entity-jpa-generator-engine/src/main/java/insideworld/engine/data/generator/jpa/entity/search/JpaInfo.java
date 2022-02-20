package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.data.generator.EntityInfo;
import insideworld.engine.entities.Entity;

public class JpaInfo implements EntityInfo {

    private final Class<? extends Entity> entity;

    private final String schema;

    private final String table;

    private final String implementation;

    private final boolean generated;

    public JpaInfo(
        final Class<? extends Entity> entity,
        final String schema,
        final String table,
        final String implementation,
        final boolean generated
    ) {
        this.entity = entity;
        this.schema = schema;
        this.table = table;
        this.implementation = implementation;
        this.generated = generated;
    }

    public String getSchema() {
        return this.schema;
    }

    public String getTable() {
        return this.table;
    }

    public Class<? extends Entity> getEntity() {
        return this.entity;
    }

    public String getImplementation() {
        return this.implementation;
    }

    public boolean isGenerated() {
        return this.generated;
    }
}
