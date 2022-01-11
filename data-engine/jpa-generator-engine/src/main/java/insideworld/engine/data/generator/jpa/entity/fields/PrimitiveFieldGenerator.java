package insideworld.engine.data.generator.jpa.entity.fields;

import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import java.beans.PropertyDescriptor;
import java.util.Date;
import javax.persistence.Column;

public class PrimitiveFieldGenerator implements FieldGenerator {

    @Override
    public void generate(final ClassCreator creator, final PropertyDescriptor descriptor) {
        final Class<?> type = descriptor.getReadMethod().getReturnType();
        final FieldCreator field = creator.getFieldCreator(descriptor.getName(), type);
        field.addAnnotation(Column.class).addValue("name", descriptor.getName());
        final MethodCreator get = creator.getMethodCreator(descriptor.getReadMethod().getName(), type);
        get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
        get.close();
        final MethodCreator set = creator.getMethodCreator(
            descriptor.getWriteMethod().getName(), void.class, descriptor.getWriteMethod().getParameterTypes()[0]);
        set.writeInstanceField(field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0));
        set.returnValue(null);
        set.close();
    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        final Class<?> type = bean.getReadMethod().getReturnType();
        return type.isPrimitive() || type.equals(String.class) || type.equals(Date.class);
    }
}
