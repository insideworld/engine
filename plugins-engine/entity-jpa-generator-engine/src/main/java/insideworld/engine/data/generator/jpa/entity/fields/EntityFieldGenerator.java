package insideworld.engine.data.generator.jpa.entity.fields;

import com.google.common.base.CaseFormat;
import insideworld.engine.data.generator.AbstractFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator extends AbstractFieldGenerator<JpaInfo> {

    private final Map<Class<? extends Entity>, JpaInfo> implementations;

    public EntityFieldGenerator(final Map<Class<? extends Entity>, JpaInfo> implementations) {
        this.implementations = implementations;
    }

    @Override
    public boolean can(final PropertyDescriptor bean, final JpaInfo info) {
        return Entity.class.isAssignableFrom(this.propertyType(bean, info));
    }

    @Override
    protected void addAnnotations(
        final FieldCreator field, final PropertyDescriptor descriptor, final JpaInfo info) {
        field.addAnnotation(ManyToOne.class);
        final String name =
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, descriptor.getName()) + "_id";
        field.addAnnotation(JoinColumn.class).addValue("name", name);
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor, final JpaInfo info) {
        final Class<?> type = this.propertyType(descriptor, info);
        if (!this.implementations.containsKey(type)) {
            throw new RuntimeException(
                String.format("Implementation for %s not found. Field: %s Class: %s ",
                    type.getName(),
                    descriptor.getName(),
                    info.getEntity().getName())
            );
        }
        return this.implementations
            .get(type)
            .getImplementation();
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
