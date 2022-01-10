package insideworld.engine.data.jpa.generator.search;

import com.google.common.collect.Lists;
import insideworld.engine.data.jpa.generator.annotations.GenerateJpaEntity;
import insideworld.engine.data.jpa.generator.annotations.GeneratesJpa;
import insideworld.engine.reflection.Reflection;

import java.util.Collection;

public class SearchMixin implements SearchEntities {

    private final Reflection reflections;

    public SearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<ToGenerate> search() {
        final Collection<ToGenerate> results = Lists.newLinkedList();
        for (final var mixin : this.reflections.getSubTypesOf(GeneratesJpa.class)) {
            for (final GenerateJpaEntity annotation : mixin.getAnnotationsByType(GenerateJpaEntity.class)) {
              results.add(new ToGenerate(annotation.entity(), annotation.schema(), annotation.table()));
            }
        }
        return results;
    }
}
