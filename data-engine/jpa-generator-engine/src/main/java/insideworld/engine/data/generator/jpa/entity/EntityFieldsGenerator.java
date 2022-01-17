package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.FieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.entities.Entity;
import io.quarkus.gizmo.ClassCreator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;

public class EntityFieldsGenerator {

    private Collection<FieldGenerator> generators;

    public EntityFieldsGenerator(final Map<Class<? extends Entity>, String> entities) {
        this.generators = EntityFieldsGenerator.createGenerators(entities);
    }

    public void createFields(final Map<Class<? extends Entity>, ClassCreator> classes) {
        classes.forEach(this::createFields);
    }

    public void createFields(final Class<? extends Entity> entity,
                             final ClassCreator creator) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
        for (final PropertyDescriptor bean : beans) {
            for (final FieldGenerator generator : this.generators) {
                if (generator.can(bean)) {
                    generator.generate(creator, bean);
                    break;
                }
            }
        }
    }

    private static Collection<FieldGenerator> createGenerators(
        final Map<Class<? extends Entity>, String> entities) {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(entities)
        );
    }

}
