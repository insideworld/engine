package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.data.generator.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.FieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.entities.Entity;

import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;

public class EntityGenerator {

    private final Map<Class<? extends Entity>, String> exists;
    private final ClassOutput output;

    public EntityGenerator(final Map<Class<? extends Entity>, String> exists,
                           final ClassOutput output) {
        this.exists = exists;
        this.output = output;
    }

    public Map<Class<? extends Entity>, String> generate(
        final Collection<JpaInfo> infos) {
        var creators = infos.stream()
            .map(info -> Pair.<Class<? extends Entity>, ClassCreator>of(
                info.getEntity(),
                this.createEntity(info))
            ).collect(Collectors.toList());
        final Map<Class<? extends Entity>, String> created = creators.stream().collect(
            Collectors.toMap(
                Pair::getLeft,
                creator -> creator.getRight().getClassName()
            )
        );
        final var generators = this.createGenerators(created);
        for (final var creator : creators) {
            this.createFields(creator.getLeft(), creator.getRight(), generators);
            creator.getRight().close();
        }
        return created;
    }

    private ClassCreator createEntity(final JpaInfo info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(this.name(info.getEntity()))
            .superClass(AbstractEntity.class)
            .interfaces(info.getEntity())
            .build();
        creator.addAnnotation(Dependent.class);
        creator.addAnnotation(javax.persistence.Entity.class);
        AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
        annotationCreator.addValue("name", info.getTable());
        annotationCreator.addValue("schema", info.getSchema());
        return creator;
    }

    private void createFields(final Class<? extends Entity> entity,
                              final ClassCreator creator,
                              final Collection<FieldGenerator> generators) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (final PropertyDescriptor bean : beans) {
            for (final FieldGenerator generator : generators) {
                if (generator.can(bean)) {
                    generator.generate(creator, bean);
                    break;
                }
            }
        }

    }

    private String name(final Class<?> entity) {
        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }


    private Collection<FieldGenerator> createGenerators(
        final Map<Class<? extends Entity>, String> created) {
        final Map<Class<? extends Entity>, String> all = Maps.newHashMapWithExpectedSize(
            this.exists.size() + created.size()
        );
        all.putAll(created);
        all.putAll(this.exists);
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(all)
        );
    }


}
