package insideworld.engine.data.generator.jpa.entity.fields;


import insideworld.engine.data.generator.jpa.entity.EntityGenerator;
import io.quarkus.gizmo.ClassCreator;
import java.beans.PropertyDescriptor;

/**
 * Generate an entity implementation.
 */
public class EntityFieldGenerator implements FieldGenerator {

    private EntityGenerator generator;

    public EntityFieldGenerator(final EntityGenerator generator) {
        this.generator = generator;
    }


    @Override
    public void generate(final ClassCreator creator, final PropertyDescriptor descriptor) {

    }

    @Override
    public boolean can(final PropertyDescriptor bean) {
        return false;
    }
}
