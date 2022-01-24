package insideworld.engine.data.generator.jpa.actions.write.search;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagInfoImpl;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.data.generator.jpa.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.data.generator.jpa.actions.write.annotations.GenerateWriteAction;
import insideworld.engine.reflection.Reflection;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class SearchWriteMixin implements SearchWriteAction {

    private final Reflection reflections;

    public SearchWriteMixin(final Reflection reflections) {
        this.reflections = reflections;
    }

    @Override
    public Collection<ActionTagInfo> search() {
        return this.reflections.getSubTypesOf(GenerateMixin.class).stream()
            .map(mixin -> mixin.getAnnotationsByType(GenerateWriteAction.class))
            .flatMap(Arrays::stream)
            .map(annotation -> new ActionTagInfoImpl(
                    annotation.entity(),
                    annotation.key(),
                    annotation.tag(),
                    annotation.interfaces()
                )
            )
            .collect(Collectors.toList());
    }

}
