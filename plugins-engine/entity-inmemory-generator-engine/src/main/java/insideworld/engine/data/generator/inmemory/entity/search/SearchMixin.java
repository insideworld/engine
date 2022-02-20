package insideworld.engine.data.generator.inmemory.entity.search;

import insideworld.engine.data.generator.inmemory.entity.annotations.GenerateInMemoryEntity;
import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<InMemoryInfo, GenerateInMemoryEntity>
    implements SearchEntities {

    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected Class<GenerateInMemoryEntity> annotation() {
        return GenerateInMemoryEntity.class;
    }

    @Override
    protected InMemoryInfo createSearch(
        final GenerateInMemoryEntity annotation, final Class<? extends GenerateMixin> mixin) {
        return new InMemoryInfo(annotation.entity(), this.name(annotation.entity(), mixin));
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(mixin.getPackageName() + ".generated.inmemory.%s", entity.getSimpleName());
    }
}
