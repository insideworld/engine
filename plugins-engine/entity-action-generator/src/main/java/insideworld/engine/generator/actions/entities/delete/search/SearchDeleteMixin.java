package insideworld.engine.generator.actions.entities.delete.search;

import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.actions.entities.abstracts.info.ActionTagsInfo;
import insideworld.engine.generator.actions.entities.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.generator.actions.entities.delete.annotations.GenerateDeleteAction;
import insideworld.engine.reflection.Reflection;

public class SearchDeleteMixin
    extends AbstractSearchMixin<ActionTagsInfo, GenerateDeleteAction>
    implements SearchDeleteAction {


    public SearchDeleteMixin(final Reflection reflections) {
        super(reflections);
    }


    @Override
    protected ActionTagsInfo createSearch(
        final GenerateDeleteAction annotation, final Class<? extends GenerateMixin> mixin) {
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
            mixin.getPackageName() + ".generated.actions.entity.Delete%sAction",
            entity.getSimpleName().replace("Entity","")
        );
    }

    @Override
    protected Class<GenerateDeleteAction> annotation() {
        return GenerateDeleteAction.class;
    }

}
