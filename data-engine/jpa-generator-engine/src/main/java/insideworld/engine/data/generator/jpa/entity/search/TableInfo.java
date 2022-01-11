package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.entities.Entity;

public class TableInfo {

    private final String schema;

    private final String table;

    public TableInfo(final String schema, final String table) {
        this.schema = schema;
        this.table = table;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }
}
