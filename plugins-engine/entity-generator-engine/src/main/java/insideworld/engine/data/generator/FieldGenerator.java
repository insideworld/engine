package insideworld.engine.data.generator;

import io.quarkus.gizmo.ClassCreator;
import java.beans.PropertyDescriptor;

public interface FieldGenerator<T extends EntityInfo> {

    void generate(ClassCreator creator, PropertyDescriptor descriptor, T info);

    boolean can(PropertyDescriptor bean, T info);
}
