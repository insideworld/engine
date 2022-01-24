package insideworld.engine.data.generator.jpa.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.entity.search.SearchEntities;
import insideworld.engine.data.generator.jpa.entity.search.SearchExists;
import insideworld.engine.data.generator.jpa.entity.search.SearchMixin;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.Table;
import org.apache.commons.lang3.tuple.Pair;

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

    public Map<Class<? extends Entity>, JpaInfo> findAndGenerate() {
        final Map<Class<? extends Entity>, JpaInfo> infos = this.findInfos().stream()
            .collect(Collectors.toMap(JpaInfo::getEntity, Function.identity()));
        final EntityClassGenerator entities = new EntityClassGenerator(this.output);
        final EntityFieldsGenerator fields = new EntityFieldsGenerator(infos);
        for (final JpaInfo info : infos.values()) {
            if (!info.isGenerated()) {
                continue;
            }
            final ClassCreator creator = entities.createEntity(info);
            fields.createFields(creator, info);
            creator.close();
        }
        return infos;
    }

    private Collection<JpaInfo> findInfos() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection, this.packages),
            new SearchExists(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
