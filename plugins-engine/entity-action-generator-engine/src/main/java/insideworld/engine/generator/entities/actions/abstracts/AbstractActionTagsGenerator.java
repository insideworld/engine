package insideworld.engine.generator.entities.actions.abstracts;

import com.google.common.collect.ImmutableList;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.entities.tags.EntitiesTag;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.function.BiConsumer;

public abstract class AbstractActionTagsGenerator<T extends ActionTagsInfo>
    extends AbstractActionTagGenerator<T> {

    public AbstractActionTagsGenerator(final ClassOutput output) {
        super(output);
    }

    @Override
    protected Collection<BiConsumer<ClassCreator, T>> methodPredicates() {
        return ImmutableList.<BiConsumer<ClassCreator, T>>builder()
            .addAll(super.methodPredicates())
            .add(this::createTags)
            .build();
    }

    private void createTags(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getTags", EntitiesTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntitiesTag.class, String.class),
            method.load(info.tags())
        );
        method.returnValue(result);
    }

}
