package insideworld.engine.data.generator.jpa.actions.abstracts;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.entities.tags.EntityTag;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.function.BiConsumer;

public abstract class AbstractActionTagGenerator<T extends ActionTagInfo> extends AbstractActionGenerator<T> {

    public AbstractActionTagGenerator(final ClassOutput output, final String packages) {
        super(output, packages);
    }

    @Override
    protected Collection<BiConsumer<ClassCreator, T>> methodPredicates() {
        return ImmutableList.of(this::createTag);
    }

    private void createTag(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getTag", EntityTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntityTag.class, String.class),
            method.load(info.tag())
        );
        method.returnValue(result);
    }

}
