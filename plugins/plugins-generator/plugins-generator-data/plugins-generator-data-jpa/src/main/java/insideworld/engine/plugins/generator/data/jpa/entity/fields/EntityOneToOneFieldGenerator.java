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
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.plugins.generator.data.base.AbstractFieldGenerator;
import insideworld.engine.plugins.generator.data.jpa.entity.search.JpaInfo;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.DotName;
import org.jboss.jandex.Type;

/**
 * Generate an entity implementation.
 */
public class EntityOneToOneFieldGenerator extends EntityFieldGenerator {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntityOneToOneFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        super(implementations);
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        return Entity.class.isAssignableFrom(this.propertyType(bean, info))
               && info.getOnetoone().equals(bean.getName());
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        final String name;
        field.addAnnotation(Id.class);
        final var annotation = field.addAnnotation(OneToOne.class);
        field.addAnnotation(JoinColumn.class).addValue("name", "id");
        annotation.addValue(
            "targetEntity",
            AnnotationValue.createClassValue(
                "targetEntity",
                Type.create(
                    DotName.createSimple(
                        this.implementations
                            .get(this.propertyType(descriptor, info))
                            .getImplementation()
                    ),
                    Type.Kind.CLASS
                )
            )
        );
    }

    @Override
    protected void additional(
        final ClassCreator creator,
        final FieldCreator field,
        final PropertyDescriptor descriptor,
        final JpaInfo info
    ) {
        final MethodCreator getter = creator.getMethodCreator("getId", long.class);
        final MethodDescriptor method = MethodDescriptor.ofMethod(
            Entity.class,
            "getId",
            long.class
        );
        getter.returnValue(
            getter.invokeInterfaceMethod(
                method,
                getter.readInstanceField(
                    field.getFieldDescriptor(),
                    getter.getThis()
                )
            )
        );
        getter.close();
    }

}
