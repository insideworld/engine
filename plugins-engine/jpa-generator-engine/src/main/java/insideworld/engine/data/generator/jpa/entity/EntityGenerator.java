package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityGenerator {

    private final ClassOutput output;
    private final Reflection reflection;
    private final String packages;

    public EntityGenerator(final ClassOutput output,
                           final Reflection reflection,
                           final String packages) {
        this.output = output;
        this.reflection = reflection;
        this.packages = packages;
    }


    public Map<Class<? extends Entity>, String> findAndGenerate() {
        final EntityClassGenerator entities =
            new EntityClassGenerator(this.output, this.findInfos(), this.packages);
        final Map<Class<? extends Entity>, ClassCreator> generated = entities.generate();
        final Map<Class<? extends Entity>, String> exists = this.findExists();
        exists.putAll(
            generated.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getClassName()
        )));
        final EntityFieldsGenerator fields = new EntityFieldsGenerator(exists);
        fields.createFields(generated);
        generated.values().forEach(ClassCreator::close);
        return exists;
    }

    private Collection<JpaInfo> findInfos() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private Map<Class<? extends Entity>, String> findExists() {
        final List<Class<? extends Entity>> interfaces = this.reflection.getSubTypesOf(Entity.class)
            .stream().filter(Class::isInterface).collect(Collectors.toList());
        final Map<Class<? extends Entity>, String> result =
            Maps.newHashMapWithExpectedSize(interfaces.size());
        for (final Class<? extends Entity> type : interfaces) {
            this.reflection.getSubTypesOf(type).stream().filter(
                entity -> entity.isAnnotationPresent(javax.persistence.Entity.class)
            ).findFirst().ifPresent(entity -> result.put(type, entity.getName()));
        }
        return result;
    }

}
