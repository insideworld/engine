package insideworld.engine.data.generator.jpa.entity.fields;

import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.SignatureElement;
import java.beans.PropertyDescriptor;

public abstract class AbstractFieldGenerator implements FieldGenerator {

    @Override
    public void generate(final ClassCreator creator,
                         final PropertyDescriptor descriptor,
                         final JpaInfo info) {
        final FieldCreator field = creator.getFieldCreator(
            descriptor.getName(),
            this.defineType(descriptor)
        );
        if (field instanceof SignatureElement) {
            ((SignatureElement<?>) field).setSignature(this.fieldSignature(descriptor));
        }
        this.addAnnotations(field, descriptor, info);
        if (descriptor.getReadMethod() != null) {
            final MethodCreator get = creator.getMethodCreator(
                descriptor.getReadMethod().getName(),
                descriptor.getReadMethod().getReturnType()
            );
            get.setSignature(this.readSignature(descriptor));
            get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
            get.close();
        }
        if (descriptor.getWriteMethod() != null) {
            final MethodCreator set = creator.getMethodCreator(
                descriptor.getWriteMethod().getName(),
                void.class,
                descriptor.getWriteMethod().getParameterTypes()[0]
            );
            set.setSignature(this.writeSignature(descriptor));
            set.writeInstanceField(field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0));
            set.returnValue(null);
            set.close();
        }
    }

    protected abstract void addAnnotations(FieldCreator field, PropertyDescriptor descriptor, JpaInfo info);

    protected abstract String defineType(PropertyDescriptor descriptor);

    protected abstract String fieldSignature(PropertyDescriptor descriptor);

    protected abstract String readSignature(PropertyDescriptor descriptor);

    protected abstract String writeSignature(PropertyDescriptor descriptor);

}
