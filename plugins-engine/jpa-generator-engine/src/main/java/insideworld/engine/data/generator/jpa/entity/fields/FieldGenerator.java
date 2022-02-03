package insideworld.engine.data.generator.jpa.entity.fields;

import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import io.quarkus.gizmo.ClassCreator;
import java.beans.PropertyDescriptor;
import org.apache.commons.lang3.tuple.Pair;

public interface FieldGenerator {

    void generate(ClassCreator creator, PropertyDescriptor descriptor, JpaInfo info);

    boolean can(PropertyDescriptor bean, JpaInfo info);
}
