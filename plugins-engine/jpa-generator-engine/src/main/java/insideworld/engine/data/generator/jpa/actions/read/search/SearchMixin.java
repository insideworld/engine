package insideworld.engine.data.generator.jpa.actions.read.search;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import insideworld.engine.reflection.Reflection;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchMixin implements SearchReadAction {

    private final Reflection reflections;

    public SearchMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<GenerateReadAction> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> mixin.getAnnotationsByType(GenerateReadAction.class))
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());
    }

}
