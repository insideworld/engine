package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.FieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.ClassCreator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;

public class EntityFieldsGenerator {

    private Collection<FieldGenerator> generators;

    public EntityFieldsGenerator(final Map<Class<? extends Entity>, JpaInfo> entities) {
        this.generators = EntityFieldsGenerator.createGenerators(entities);
    }


    public void createFields(final ClassCreator creator, final JpaInfo info) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(info.getEntity())
                .getPropertyDescriptors();
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
        for (final PropertyDescriptor bean : beans) {
            for (final FieldGenerator generator : this.generators) {
                if (generator.can(bean)) {
                    generator.generate(creator, bean, info);
                    break;
                }
            }
        }
    }

    private static Collection<FieldGenerator> createGenerators(
        final Map<Class<? extends Entity>, JpaInfo> entities) {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(entities),
            new EntityFieldGenerator(entities)
        );
    }

}
