package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.entities.Entity;

public class JpaInfo {

    private final Class<? extends Entity> entity;

    private final String schema;

    private final String table;

    public JpaInfo(final Class<? extends Entity> entity, final String schema, final String table) {
        this.entity = entity;
        this.schema = schema;
        this.table = table;
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
}
