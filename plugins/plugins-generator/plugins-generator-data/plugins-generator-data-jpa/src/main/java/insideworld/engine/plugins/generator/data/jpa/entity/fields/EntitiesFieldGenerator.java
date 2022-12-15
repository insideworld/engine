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

package insideworld.engine.plugins.generator.data.jpa.entity.fields;

import com.google.common.base.CaseFormat;
import insideworld.engine.plugins.generator.data.base.AbstractFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.search.JpaInfo;
import insideworld.engine.core.data.core.Entity;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import javax.persistence.OneToMany;

public class EntitiesFieldGenerator extends AbstractFieldGenerator<JpaInfo> {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntitiesFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        final Class<?> ret = this.propertyType(bean, info);
        return Collection.class.isAssignableFrom(ret)
            && Entity.class.isAssignableFrom(this.getGeneric(bean));
    }


    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        final AnnotationCreator annotation = field.addAnnotation(OneToMany.class);
        final String name =
            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, info.getTable() + "_id");
        annotation.addValue("mappedBy", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final JpaInfo info) {
        return Collection.class.getName();
    }

    @Override
    protected String fieldSignature(final PropertyDescriptor descriptor) {
        final Class<?> generic = this.getGeneric(descriptor);
        return String.format("Ljava/util/Collection<L%s;>;",
            this.implementations.get(generic).getImplementation().replace(".","/")
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
