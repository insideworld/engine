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

    private final Map<Class<?>, String> exists;
    private final Collection<FieldGenerator> generators;
    private final ClassOutput output;


    public EntityGenerator(final Map<Class<?>, String> exists,
                           final ClassOutput output) {
        this.exists = exists;
        this.output = output;
        this.generators = this.createGenerators();
    }

    public Map<Class<?>, String> generate(final Collection<SearchEntities> searchers) {
        final Collection<JpaInfo> infos = searchers.stream()
            .flatMap(searcher -> searcher.search().stream())
            .collect(Collectors.toList());
        var creators = infos.stream()
            .map(info -> Pair.of(info.getEntity(), this.createEntity(info)))
            .collect(Collectors.toList());
        for (final var creator : creators) {
            this.createFields(creator.getLeft(), creator.getRight());
            creator.getRight().close();
        }
        return creators.stream().collect(Collectors.toMap(
            Pair::getLeft,
            creator -> creator.getRight().getClassName()
        ));
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

    private void createFields(final Class<? extends Entity> entity, final ClassCreator creator) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(entity).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
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

    private String name(final Class<?> entity) {
        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }


    private Collection<FieldGenerator> createGenerators() {
        return ImmutableList.of(
            new PrimitiveFieldGenerator(),
            new EntitiesFieldGenerator(),
            new EntityFieldGenerator(this)
        );
    }

//    private Map<Class<?>, String> findExists() {
//        final List<Class<? extends Entity>> interfaces = this.reflection.getSubTypesOf(Entity.class)
//            .stream().filter(Class::isInterface).collect(Collectors.toList());
//        final Map<Class<?>, String> result = Maps.newHashMapWithExpectedSize(interfaces.size());
//        for (final Class<? extends Entity> type : interfaces) {
//            this.reflection.getSubTypesOf(type).stream().filter(
////                entity -> !entity.isInterface() && !Modifier.isAbstract(entity.getModifiers())
//                entity -> entity.isAnnotationPresent(javax.persistence.Entity.class)
//            ).findFirst().ifPresent(entity -> result.put(type, entity.getName()));
//        }
//        return result;
//    }
}
