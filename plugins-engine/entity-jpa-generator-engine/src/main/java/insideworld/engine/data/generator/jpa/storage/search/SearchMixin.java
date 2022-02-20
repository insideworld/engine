package insideworld.engine.data.generator.jpa.storage.search;

import insideworld.engine.data.generator.StorageInfo;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<StorageInfo, GenerateCrud>
    implements SearchStorages {

    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected StorageInfo createSearch(
        final GenerateCrud annotation, final Class<? extends GenerateMixin> mixin) {
        return new StorageInfo(
            annotation.entity(),
            annotation.override(),
            this.name(annotation.entity(), mixin)
        );
    }

    @Override
    protected Class<GenerateCrud> annotation() {
        return GenerateCrud.class;
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(
            mixin.getPackageName() + ".generated.storage.crud.%sStorage",
            entity.getSimpleName()
        );
    }
}
