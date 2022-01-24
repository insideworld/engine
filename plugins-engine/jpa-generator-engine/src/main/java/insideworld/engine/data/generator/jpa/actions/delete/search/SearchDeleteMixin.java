package insideworld.engine.data.generator.jpa.actions.delete.search;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.data.generator.jpa.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.data.generator.jpa.actions.delete.annotations.GenerateDeleteActions;
import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import insideworld.engine.reflection.Reflection;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchDeleteMixin implements SearchDeleteAction {

    private final Reflection reflections;

    public SearchDeleteMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<ActionTagsInfo> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> mixin.getAnnotationsByType(GenerateDeleteAction.class))
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
