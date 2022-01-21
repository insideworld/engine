package insideworld.engine.data.generator.jpa.entity.fields;

import io.quarkus.gizmo.ClassCreator;
import java.beans.PropertyDescriptor;

public class EntitiesFieldGenerator implements FieldGenerator {
    @Override
    public void generate(ClassCreator creator, PropertyDescriptor descriptor) {

    }

    @Override
    public boolean can(PropertyDescriptor bean) {
        return false;
    }
}
