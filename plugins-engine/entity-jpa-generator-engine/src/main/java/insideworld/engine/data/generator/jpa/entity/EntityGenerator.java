package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchExists;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.entities.Entity;
import insideworld.engine.generator.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntityGenerator {

    private final ClassOutput output;
    private final Reflection reflection;

    public EntityGenerator(final ClassOutput output,
                           final Reflection reflection) {
        this.output = output;
        this.reflection = reflection;
    }

    public Map<Class<? extends Entity>, JpaInfo> findAndGenerate() {
        final Map<Class<? extends Entity>, JpaInfo> infos = this.findInfos().stream()
            .collect(Collectors.toMap(JpaInfo::getEntity, Function.identity()));
        final EntityClassGenerator entity = new EntityClassGenerator(this.output);
        final EntityFieldsGenerator fields = new EntityFieldsGenerator(infos);
        for (final JpaInfo info : infos.values()) {
            if (!info.isGenerated()) {
                continue;
            }
            try {
                final ClassCreator creator = entity.createEntity(info);
                fields.createFields(creator, info);
                creator.close();
            } catch (final Exception exp) {
                throw new RuntimeException(
                    "Can't create entity for type" + info.getEntity().getName(),
                    exp
                );
            }
        }
        return infos;
    }

    private Collection<JpaInfo> findInfos() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection),
            new SearchExists(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}