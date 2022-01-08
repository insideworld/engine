package insideworld.engine.data.jpa.generator.search;

import insideworld.engine.entities.Entity;

public class ToGenerate {

    private final Class<? extends Entity> entity;

    private final String schema;

    private final String table;

    public ToGenerate(
            final Class<? extends Entity> entity,
            final String schema,
            final String table
    ) {
        this.entity = entity;
        this.schema = schema;
        this.table = table;
    }

    public Class<? extends Entity> getEntity() {
        return entity;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }
}
