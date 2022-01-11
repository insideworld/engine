package insideworld.engine.data.generator.jpa.entity.fields;

import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import java.beans.PropertyDescriptor;
import java.util.Date;
import javax.persistence.Column;

public class PrimitiveFieldGenerator extends AbstractFieldGenerator {

    @Override
    protected void addAnnotations(final FieldCreator field, final PropertyDescriptor descriptor) {
        field.addAnnotation(Column.class).addValue("name", descriptor.getName());
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor) {
        return descriptor.getReadMethod().getReturnType().getName();
    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        final Class<?> type = bean.getReadMethod().getReturnType();
        return type.isPrimitive() || type.equals(String.class) || type.equals(Date.class);
    }


}
