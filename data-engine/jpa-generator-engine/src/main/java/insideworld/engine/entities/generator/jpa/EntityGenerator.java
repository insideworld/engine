package insideworld.engine.entities.generator.jpa;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generator.jpa.fields.FieldGenerator;

import java.util.Collection;
import java.util.Map;

public class EntityGenerator {

    private final Map<Class<?>, String> exists;
    private final Collection<FieldGenerator> generators;

    public EntityGenerator(final Map<Class<?>, String> exists,
                           final Collection<FieldGenerator> generators) {
        this.exists = exists;
        this.generators = generators;
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
}
