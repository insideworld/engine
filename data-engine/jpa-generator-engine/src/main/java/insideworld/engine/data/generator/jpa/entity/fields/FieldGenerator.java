package insideworld.engine.data.generator.jpa.entity.fields;

import io.quarkus.gizmo.ClassCreator;
import java.beans.PropertyDescriptor;

public interface FieldGenerator {

    void generate(ClassCreator creator, PropertyDescriptor descriptor);

    boolean can(PropertyDescriptor bean);
}
