/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.data.generator.jpa.entity.fields;

import com.google.common.base.CaseFormat;
import insideworld.engine.data.generator.AbstractFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator extends AbstractFieldGenerator<JpaInfo> {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntityFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        return Entity.class.isAssignableFrom(this.propertyType(bean, info));
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        field.addAnnotation(ManyToOne.class);
        final String name =
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, descriptor.getName()) + "_id";
        field.addAnnotation(JoinColumn.class).addValue("name", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final JpaInfo info) {
        final Class<?> type = this.propertyType(descriptor, info);
        if (!this.implementations.containsKey(type)) {
            throw new RuntimeException(
                String.format("Implementation for %s not found. Field: %s Class: %s ",
                    type.getName(),
                    descriptor.getName(),
                    info.getEntity().getName())
            );
        }
        return this.implementations
            .get(type)
            .getImplementation();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        return null;
    }

    @Override
    protected String readSignature(PropertyDescriptor descriptor) {
        return null;
    }

    @Override
    protected String writeSignature(PropertyDescriptor descriptor) {
        return null;
    }


}
