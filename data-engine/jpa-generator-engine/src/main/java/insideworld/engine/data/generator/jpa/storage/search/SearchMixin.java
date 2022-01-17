package insideworld.engine.data.generator.jpa.storage.search;

import insideworld.engine.data.generator.jpa.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.annotations.JpaGenerator;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.storage.CrudGenerator;
import insideworld.engine.data.generator.jpa.storage.GenerateCrud;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchMixin implements SearchStorages {

    private final Reflection reflections;

    public SearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<Class<? extends Entity>> search() {
        return this.reflections.getSubTypesOf(CrudGenerator.class).stream()
            .map(mixin -> mixin.getAnnotationsByType(GenerateCrud.class))
            .flatMap(Arrays::stream)
            .map(GenerateCrud::entity)
            .collect(Collectors.toList());
    }

}
