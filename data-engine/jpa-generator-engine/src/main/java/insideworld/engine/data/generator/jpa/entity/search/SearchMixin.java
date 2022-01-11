package insideworld.engine.data.generator.jpa.entity.search;

import com.google.common.collect.Lists;
import insideworld.engine.data.generator.jpa.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.annotations.GeneratesJpa;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Table;

public class SearchMixin implements SearchEntities {

    private final Reflection reflections;

    public SearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Map<Class<? extends Entity>, TableInfo> search() {
        return
            this.reflections.getSubTypesOf(GeneratesJpa.class).stream()
                .map(mixin -> mixin.getAnnotationsByType(GenerateJpaEntity.class))
                .flatMap(Arrays::stream)
                .collect(Collectors.toMap(
                    GenerateJpaEntity::entity,
                    annotation -> new TableInfo(annotation.schema(), annotation.table())
                ));
    }
}
