package insideworld.engine.data.generator.inmemory.entity.fields;


import insideworld.engine.data.generator.AbstractFieldGenerator;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import insideworld.engine.entities.Entity;
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


}
