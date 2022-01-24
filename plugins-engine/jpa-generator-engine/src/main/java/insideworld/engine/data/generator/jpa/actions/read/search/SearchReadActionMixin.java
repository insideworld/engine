package insideworld.engine.data.generator.jpa.actions.read.search;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import insideworld.engine.reflection.Reflection;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchReadActionMixin implements SearchReadAction {

    private final Reflection reflections;

    public SearchReadActionMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<ActionTagsInfo> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> mixin.getAnnotationsByType(GenerateReadAction.class))
            .flatMap(Arrays::stream)
            .map(annotation -> new ActionTagsInfoImpl(
                    annotation.entity(),
                    annotation.key(),
                    annotation.tag(),
                    annotation.tags(),
                    annotation.interfaces()
                )
            )
            .collect(Collectors.toList());
    }

}
