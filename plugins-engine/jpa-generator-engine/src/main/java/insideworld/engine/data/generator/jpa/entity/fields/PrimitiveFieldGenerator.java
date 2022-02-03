package insideworld.engine.data.generator.jpa.entity.fields;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Primitives;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Date;
import javax.persistence.Column;

public class PrimitiveFieldGenerator extends AbstractFieldGenerator {

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        final Class<?> type = this.propertyType(bean, info);
        return type.isPrimitive() || type.equals(String.class) || type.equals(Date.class) || Primitives.isWrapperType(type);
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        final String name = CaseFormat.LOWER_CAMEL.to(
            CaseFormat.LOWER_UNDERSCORE, descriptor.getName());
        field.addAnnotation(Column.class).addValue("name", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final JpaInfo info) {
        return this.propertyType(descriptor, info).getName();
    }

    @Override
    protected String fieldSignature(PropertyDescriptor descriptor) {
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
