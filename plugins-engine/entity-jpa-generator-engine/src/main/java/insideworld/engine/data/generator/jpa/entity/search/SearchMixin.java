package insideworld.engine.data.generator.jpa.entity.search;

import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.generator.AbstractSearchMixin;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.reflection.Reflection;

public class SearchMixin
    extends AbstractSearchMixin<JpaInfo,GenerateJpaEntity>
    implements SearchEntities {


    public SearchMixin(final Reflection reflections) {
        super(reflections);
    }

    @Override
    protected Class<GenerateJpaEntity> annotation() {
        return GenerateJpaEntity.class;
    }

    @Override
    protected JpaInfo createSearch(
        final GenerateJpaEntity annotation, final Class<? extends GenerateMixin> mixin) {
        return new JpaInfo(
            annotation.entity(),
            annotation.schema(),
            annotation.table(),
            this.name(annotation.entity(), mixin),
            true
        );
    }

    private String name(final Class<?> entity, final Class<? extends GenerateMixin> mixin) {
        return String.format(mixin.getPackageName() + ".generated.jpa.%s", entity.getSimpleName());
    }
}
