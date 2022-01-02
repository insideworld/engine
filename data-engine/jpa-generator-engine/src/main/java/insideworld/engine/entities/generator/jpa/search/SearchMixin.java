package insideworld.engine.entities.generator.jpa.search;

import com.google.common.collect.Lists;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generator.jpa.annotations.GenerateJpaEntity;
import insideworld.engine.entities.generator.jpa.annotations.GeneratesJpa;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.LinkedList;

public class SearchMixin implements SearchEntities {

    private final Reflections reflections;

    public SearchMixin(final Reflections reflections) {
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
