package insideworld.engine.data.generator.inmemory.entity.fields;

import com.google.common.base.CaseFormat;
import com.google.common.primitives.Primitives;
import insideworld.engine.data.generator.AbstractFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Date;

public class PrimitiveFieldGenerator extends AbstractFieldGenerator<InMemoryInfo> {

    @Override
    public boolean can(final PropertyDescriptor bean, final InMemoryInfo info) {
        final Class<?> type = this.propertyType(bean, info);
        return type.isPrimitive() || type.equals(String.class) || type.equals(Date.class) || Primitives.isWrapperType(type);
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final InMemoryInfo info) {
        //Nothing to do.
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final InMemoryInfo info) {
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
