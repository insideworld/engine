package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntityFacade;
import insideworld.engine.data.jpa.AbstractEntity;
import insideworld.engine.data.generator.jpa.entity.fields.EntitiesFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.EntityFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.FieldGenerator;
import insideworld.engine.data.generator.jpa.entity.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.data.generator.jpa.entity.search.TableInfo;
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

import insideworld.engine.reflection.Reflection;
import javax.enterprise.context.Dependent;
import javax.persistence.Table;

public class EntityGenerator {

    private final Map<Class<?>, String> exists;
    private final Map<Class<? extends Entity>, TableInfo> generates;
    private final ClassOutput output;
    private final Collection<FieldGenerator> generators;

    public EntityGenerator(final Map<Class<? extends Entity>, TableInfo> generates,
                           final Map<Class<?>, String> exists,
                           final ClassOutput output) {
        this.generates = generates;
        this.exists = exists;
        this.output = output;
        this.generators = this.createGenerators();
    }

    public void generate() {
        for (final Class<? extends Entity> entity : this.generates.keySet()) {
            this.generate(entity);
        }
    }

    public String generate(final Class<? extends Entity> entity) {
        final String name;
        if (!this.exists.containsKey(entity)) {
            name = this.exists.get(entity);
        }
        return
        else {
            name = this.name(entity);
            this.exists.put(entity, name);
            final TableInfo generate = this.generates.get(entity);
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .className(name)
                .superClass(AbstractEntity.class)
                .interfaces(entity)
                .build();
            creator.addAnnotation(Dependent.class);
            creator.addAnnotation(javax.persistence.Entity.class);
            AnnotationCreator annotationCreator = creator.addAnnotation(Table.class);
            annotationCreator.addValue("name", generate.getTable());
            annotationCreator.addValue("schema", generate.getSchema());
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
            creator.close();
        }
        return name;
    }

    private String createClass(final Class<? extends Entity> entity) {

    }

    private String name(final Class<?> entity) {
        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }

    private Map<Class<? extends Entity>, TableInfo> findGenerates() {
        final Collection<TableInfo> generates = Lists.newLinkedList();
        for (final SearchEntities searcher : this.createSearchers()) {
            generates.addAll(searcher.search());
        }
        return generates.stream().collect(Collectors.toMap(
            TableInfo::getEntity,
            Function.identity()
        ));
    }

    private Collection<SearchEntities> createSearchers() {
        return ImmutableList.of(
                new SearchMixin(this.reflection)
        );
    }

    private Collection<FieldGenerator> createGenerators() {
        return ImmutableList.of(
                new PrimitiveFieldGenerator(),
                new EntitiesFieldGenerator(),
                new EntityFieldGenerator(this)
        );
    }

    private Map<Class<?>, String> findExists() {
        final List<Class<? extends Entity>> interfaces = this.reflection.getSubTypesOf(Entity.class)
                .stream().filter(Class::isInterface).collect(Collectors.toList());
        final Map<Class<?>, String> result = Maps.newHashMapWithExpectedSize(interfaces.size());
        for (final Class<? extends Entity> type : interfaces) {
            this.reflection.getSubTypesOf(type).stream().filter(
//                entity -> !entity.isInterface() && !Modifier.isAbstract(entity.getModifiers())
                    entity -> entity.isAnnotationPresent(javax.persistence.Entity.class)
            ).findFirst().ifPresent(entity -> result.put(type, entity.getName()));
        }
        return result;
    }
}
