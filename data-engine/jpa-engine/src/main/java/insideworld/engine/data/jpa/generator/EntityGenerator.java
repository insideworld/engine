package insideworld.engine.data.jpa.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.data.jpa.generator.fields.EntitiesFieldGenerator;
import insideworld.engine.data.jpa.generator.fields.EntityFieldGenerator;
import insideworld.engine.data.jpa.generator.fields.FieldGenerator;
import insideworld.engine.data.jpa.generator.fields.PrimitiveFieldGenerator;
import insideworld.engine.data.jpa.generator.search.SearchEntities;
import insideworld.engine.data.jpa.generator.search.SearchMixin;
import insideworld.engine.data.jpa.generator.search.ToGenerate;
import insideworld.engine.entities.Entity;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import insideworld.engine.reflection.Reflection;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class EntityGenerator {

    private final Map<Class<?>, String> exists;
    private final Collection<ToGenerate> generate;
    private final Collection<FieldGenerator> generators;
    private final Reflection reflection;

    public EntityGenerator(final Reflection reflection) {
//        this.exists = exists;
//        this.generators = this.createGenerators();
//        this.reflections = this.createReflection(loader);
        this.exists = this.findExists();
        this.reflection = reflection;
    }

    public void generate() {

    }

    public String generate(final Class<? extends Entity> clazz) {
        final String type;
        if (this.exists.containsKey(clazz)) {
            type = this.exists.get(clazz);
        } else {
            type = this.name(clazz);
        }
        return type;
    }

    private String name(final Class<?> entity) {
        return "insideworld.engine.entities.generated.jpa." + entity.getSimpleName();
    }

    private Collection<SearchEntities> createSearchers(final Reflection reflection) {
        return ImmutableList.of(
                new SearchMixin(reflection)
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
