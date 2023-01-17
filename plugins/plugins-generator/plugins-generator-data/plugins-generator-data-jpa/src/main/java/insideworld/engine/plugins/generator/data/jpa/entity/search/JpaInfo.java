/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.plugins.generator.data.jpa.entity.search;

import insideworld.engine.plugins.generator.data.base.EntityInfo;
import insideworld.engine.core.data.core.Entity;

public class JpaInfo implements EntityInfo {

    private final Class<? extends Entity> entity;

    private final String schema;

    private final String table;

    private final String implementation;

    private final String onetoone;
    private final boolean generated;

    public JpaInfo(
        final Class<? extends Entity> entity,
        final String schema,
        final String table,
        final String implementation,
        final String onetoone,
        final boolean generated
    ) {
        this.entity = entity;
        this.schema = schema;
        this.table = table;
        this.implementation = implementation;
        this.onetoone = onetoone;
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

    public String getOnetoone() {
        return this.onetoone;
    }
}
