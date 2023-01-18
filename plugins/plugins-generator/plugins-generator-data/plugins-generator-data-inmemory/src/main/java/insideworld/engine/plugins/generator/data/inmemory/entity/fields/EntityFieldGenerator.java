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

package insideworld.engine.plugins.generator.data.inmemory.entity.fields;


import insideworld.engine.plugins.generator.data.base.AbstractFieldGenerator;
import insideworld.engine.plugins.generator.data.inmemory.entity.search.InMemoryInfo;
import insideworld.engine.core.data.core.Entity;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator extends AbstractFieldGenerator<InMemoryInfo> {

    @Override
    public boolean can(final PropertyDescriptor bean, final InMemoryInfo info) {
        return Entity.class.isAssignableFrom(this.propertyType(bean, info));
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final InMemoryInfo info) {
        //Nothing to do
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final InMemoryInfo info) {
        return this.propertyType(descriptor, info).getName();
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

    @Override
    protected void additional(final ClassCreator creator, final FieldCreator field, final PropertyDescriptor descriptor, final InMemoryInfo info) {

    }


}
