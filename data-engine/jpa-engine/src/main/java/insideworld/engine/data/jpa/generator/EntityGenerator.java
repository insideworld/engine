package insideworld.engine.data.jpa.generator;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.jpa.generator.fields.FieldGenerator;
import insideworld.engine.data.jpa.generator.search.ToGenerate;
import insideworld.engine.entities.Entity;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

public class EntityGenerator {

    private final Map<Class<?>, String> exists;
    private final Collection<ToGenerate> generate;
    private final Collection<FieldGenerator> generators;

    public EntityGenerator(final ClassLoader loader) {
        this.exists = exists;
        this.generators = this.createGenerators();
        this.reflections = this.createReflection(loader);
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

    private Map<Class<?>, String> searchExists() {

    }

    private Reflections createReflection(final ClassLoader loader) {
        return new Reflections(
            new ConfigurationBuilder()
                .addClassLoaders(loader)
                .forPackage("insideworld", loader)
        );
    }

    private Collection<FieldGenerator> createGenerators() {
        return ImmutableList.of(

        );
    }

    private Map<Class<?>, String> findExists() {
        final List<Class<? extends Entity>> interfaces = this.reflections.getSubTypesOf(Entity.class)
            .stream().filter(Class::isInterface).collect(Collectors.toList());
        for (final Class<? extends Entity> type : interfaces) {
            this.reflections.getSubTypesOf(type).stream().filter(
                entity -> !entity.isInterface() && !Modifier.isAbstract(entity.getModifiers())
            ).findFirst().ifPresent(entity -> this.exists.put(type, entity.getName()));
        }
    }
}
