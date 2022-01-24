package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.reflection.Reflection;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchMixin implements SearchEntities {

    private final Reflection reflections;
    private final String packages;

    public SearchMixin(final Reflection reflections, final String packages) {
        this.reflections = reflections;
        this.packages = packages + ".generated.jpa.%s";
    }

    @Override
    public Collection<JpaInfo> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
                .map(mixin -> mixin.getAnnotationsByType(GenerateJpaEntity.class))
                .flatMap(Arrays::stream)
                .map(annotation -> new JpaInfo(
                    annotation.entity(),
                    annotation.schema(),
                    annotation.table(),
                    this.name(annotation.entity())))
                .collect(Collectors.toList());
    }

    private String name(final Class<?> entity) {
        return String.format(this.packages, entity.getSimpleName());
    }


}
