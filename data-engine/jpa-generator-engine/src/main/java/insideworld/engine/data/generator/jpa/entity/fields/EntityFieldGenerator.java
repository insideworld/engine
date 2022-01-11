package insideworld.engine.data.generator.jpa.entity.fields;


import insideworld.engine.data.generator.jpa.entity.EntityGenerator;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import java.beans.PropertyDescriptor;
import java.util.Map;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator extends AbstractFieldGenerator {

    private final Map<Class<? extends Entity>, String> implementations;

    public EntityFieldGenerator(final Map<Class<? extends Entity>, String> implementations) {
        this.implementations = implementations;
    }

    @Override
    protected void addAnnotations(final FieldCreator field, final PropertyDescriptor descriptor) {
        field.addAnnotation(ManyToOne.class);
        field.addAnnotation(JoinColumn.class).addValue("name", descriptor.getName() + "_id");
    }

    @Override
    protected String defineType(final PropertyDescriptor descriptor) {
        return this.implementations.get(descriptor.getReadMethod().getReturnType());
    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        return bean.getReadMethod().getReturnType().isAssignableFrom(Entity.class);
    }
}
