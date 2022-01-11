package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.data.generator.jpa.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.annotations.GeneratesJpa;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchMixin implements SearchEntities {

    private final Reflection reflections;

    public SearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<JpaInfo> search() {
        return this.reflections.getSubTypesOf(GeneratesJpa.class).stream()
                .map(mixin -> mixin.getAnnotationsByType(GenerateJpaEntity.class))
                .flatMap(Arrays::stream)
                .map(annotation -> new JpaInfo(
                    annotation.entity(), annotation.schema(), annotation.table()))
                .collect(Collectors.toList());
    }
}
