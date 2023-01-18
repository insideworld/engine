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

package insideworld.engine.plugins.generator.data.jpa.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.plugins.generator.data.base.AbstractEntityFieldsGenerator;
import insideworld.engine.plugins.generator.data.base.FieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.fields.EntityOneToOneFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.search.JpaInfo;
import insideworld.engine.core.data.core.Entity;
import java.util.Collection;
import java.util.Map;

public class EntityFieldsGenerator extends AbstractEntityFieldsGenerator<JpaInfo> {

    private final Map<Class<? extends Entity>, JpaInfo> entities;

    public EntityFieldsGenerator(final Map<Class<? extends Entity>, JpaInfo> entities) {
        this.entities = entities;
    }

    @Override
    protected Collection<FieldGenerator<JpaInfo>> createGenerators() {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(this.entities),
            new EntityFieldGenerator(this.entities),
            new EntityOneToOneFieldGenerator(this.entities)
        );
    }
}
