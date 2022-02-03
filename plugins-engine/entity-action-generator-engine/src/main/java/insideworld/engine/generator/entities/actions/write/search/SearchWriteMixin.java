package insideworld.engine.generator.entities.actions.write.search;

import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagInfoImpl;
import insideworld.engine.generator.entities.actions.write.annotations.GenerateWriteAction;
import insideworld.engine.reflection.Reflection;

public class SearchWriteMixin
    extends AbstractSearchMixin<ActionTagInfo, GenerateWriteAction>
    implements SearchWriteAction {

    public SearchWriteMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected ActionTagInfo createSearch(
        final GenerateWriteAction annotation, final Class<? extends GenerateMixin> mixin) {
        return new ActionTagInfoImpl(
            annotation.entity(),
            annotation.key(),
            annotation.tag(),
            annotation.interfaces(),
            this.name(annotation.entity(), mixin)
            );
    }

    @Override
    protected Class<GenerateWriteAction> annotation() {
        return GenerateWriteAction.class;
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(
            mixin.getPackageName() + ".generated.actions.entity.Write%sAction",
            entity.getSimpleName().replace("Entity","")
        );
    }
}
