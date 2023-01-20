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
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

public class EntitiesFieldGenerator extends AbstractFieldGenerator<InMemoryInfo> {

    @Override
    public boolean can(final PropertyDescriptor bean, final InMemoryInfo info) {
        final Class<?> ret = this.propertyType(bean, info);
        return Collection.class.isAssignableFrom(ret)
            && Entity.class.isAssignableFrom(this.getGeneric(bean));
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final InMemoryInfo info) {
        //Nothing to add.
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final InMemoryInfo info) {
        return Collection.class.getName();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        return String.format("Ljava/util/Collection<L%s;>;",
            this.getGeneric(descriptor).getName().replace(".","/")
        );
    }

    @Override
    protected String readSignature(final PropertyDescriptor descriptor) {
        return String.format(
            "()Ljava/util/Collection<L%s;>;",
            this.getGeneric(descriptor).getName().replace(".","/")
        );
    }

    @Override
    protected String writeSignature(final PropertyDescriptor descriptor) {
        return String.format(
            "(Ljava/util/Collection<L%s;>;)V",
            this.getGeneric(descriptor).getName().replace(".","/")
        );
    }


    private Class<?> getGeneric(final PropertyDescriptor descriptor) {
        final Class<?> type;
        if (descriptor.getReadMethod() != null) {
            type = (Class<?>) ((ParameterizedType)
                descriptor.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0];
        } else {
            type = (Class<?>) (((ParameterizedType)
                descriptor
                    .getWriteMethod().getGenericParameterTypes()[0]).getActualTypeArguments()[0]);
        }
        return type;
    }
}
