package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
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

import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;

public class EntityGenerator {

    private final Reflection reflection;
    private final ClassOutput output;

    public EntityGenerator(final Reflection reflection,
                           final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public Map<Class<? extends Entity>, String> defineEntities() {
        final Map<Class<? extends Entity>, String> generated = this.generate();
        final Map<Class<? extends Entity>, String> exists = this.exists();

        for (final JpaInfo jpaInfo : this.toGenerate()) {

        }
        final Map<Class<? extends Entity>, String> all = Maps.newHashMapWithExpectedSize(
            this.exists.size() + created.size()
        );


    }

    public Map<Class<? extends Entity>, String> generate() {
        final Collection<JpaInfo> infos = this.toGenerate();
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
        final var generators = this.generators(created);
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
            generators.stream()
                .filter(generator -> generator.can(bean))
                .findFirst()
                .ifPresent(generator -> generator.generate(creator, bean));
        }
    }

    private String name(final Class<?> entity) {
        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }

    private Collection<FieldGenerator> generators(
        final Map<Class<? extends Entity>, String> entities) {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(entities)
        );
    }

    private Collection<JpaInfo> toGenerate() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private Map<Class<? extends Entity>, String> exists() {
        final List<Class<? extends Entity>> interfaces = this.reflection.getSubTypesOf(Entity.class)
            .stream().filter(Class::isInterface).collect(Collectors.toList());
        final Map<Class<? extends Entity>, String> result =
            Maps.newHashMapWithExpectedSize(interfaces.size());
        for (final Class<? extends Entity> type : interfaces) {
            this.reflection.getSubTypesOf(type).stream().filter(
//                entity -> !entity.isInterface() && !Modifier.isAbstract(entity.getModifiers())
                entity -> entity.isAnnotationPresent(javax.persistence.Entity.class)
            ).findFirst().ifPresent(entity -> result.put(type, entity.getName()));
        }
        return result;
    }
}
