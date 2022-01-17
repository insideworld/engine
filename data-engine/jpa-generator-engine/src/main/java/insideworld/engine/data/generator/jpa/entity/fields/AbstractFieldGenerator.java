package insideworld.engine.data.generator.jpa.entity.fields;

import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import java.beans.PropertyDescriptor;

public abstract class AbstractFieldGenerator implements FieldGenerator {

    @Override
    public void generate(final ClassCreator creator, final PropertyDescriptor descriptor) {
        final FieldCreator field = creator.getFieldCreator(
            descriptor.getName(),
            this.defineType(descriptor)
        );
        this.addAnnotations(field, descriptor);
        if (descriptor.getReadMethod() != null) {
            final MethodCreator get = creator.getMethodCreator(
                descriptor.getReadMethod().getName(),
                descriptor.getReadMethod().getReturnType()
            );
            get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
            get.close();
        }
        if (descriptor.getWriteMethod() != null) {
            final MethodCreator set = creator.getMethodCreator(
                descriptor.getWriteMethod().getName(),
                void.class,
                descriptor.getWriteMethod().getParameterTypes()[0]
            );
            set.writeInstanceField(field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0));
            set.returnValue(null);
            set.close();
        }
    }

    protected abstract void addAnnotations(FieldCreator field, PropertyDescriptor descriptor);

    protected abstract String defineType(PropertyDescriptor descriptor);

}
