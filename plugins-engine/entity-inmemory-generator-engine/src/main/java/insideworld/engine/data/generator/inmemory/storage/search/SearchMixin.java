package insideworld.engine.data.generator.inmemory.storage.search;

import insideworld.engine.data.generator.StorageInfo;
import insideworld.engine.data.generator.inmemory.storage.annotations.GenerateInMemoryCrud;
import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<StorageInfo, GenerateInMemoryCrud>
    implements SearchStorages {

    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected StorageInfo createSearch(
        final GenerateInMemoryCrud annotation, final Class<? extends GenerateMixin> mixin) {
        return new StorageInfo(
            annotation.entity(),
            annotation.override(),
            this.name(annotation.entity(), mixin)
        );
    }

    @Override
    protected Class<GenerateInMemoryCrud> annotation() {
        return GenerateInMemoryCrud.class;
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(
            mixin.getPackageName() + ".generated.storage.inmemory.%sStorage",
            entity.getSimpleName()
        );
    }
}
