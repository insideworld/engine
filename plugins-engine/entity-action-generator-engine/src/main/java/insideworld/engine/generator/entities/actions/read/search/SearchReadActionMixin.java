package insideworld.engine.generator.entities.actions.read.search;

import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.generator.entities.actions.read.annotations.GenerateReadAction;
import insideworld.engine.reflection.Reflection;

public class SearchReadActionMixin extends AbstractSearchMixin<ActionTagsInfo, GenerateReadAction>
    implements SearchReadAction {

    public SearchReadActionMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected ActionTagsInfo createSearch(GenerateReadAction annotation, Class<? extends GenerateMixin> mixin) {
        return new ActionTagsInfoImpl(
            annotation.entity(),
            annotation.key(),
            annotation.tag(),
            annotation.tags(),
            annotation.interfaces(),
            this.name(annotation.entity(), mixin)
        );
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(
            mixin.getPackageName() + ".generated.actions.entity.Read%sAction",
            entity.getSimpleName().replace("Entity","")
        );
    }

    @Override
    protected Class<GenerateReadAction> annotation() {
        return GenerateReadAction.class;
    }


}
