package insideworld.engine.generator;

import insideworld.engine.reflection.Reflection;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractSearchMixin<T, U extends Annotation> {

    private final Reflection reflections;

    public AbstractSearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    public Collection<T> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> Pair.of(mixin, mixin.getAnnotationsByType(this.annotation())))
            .flatMap(pair -> Arrays.stream(pair.getRight()).map(
                annotation -> this.createSearch(annotation, pair.getLeft())
            ))
            .collect(Collectors.toList());
    }

    protected abstract T createSearch(U annotation, Class<? extends GenerateMixin> mixin);

    protected abstract Class<U> annotation();

}
