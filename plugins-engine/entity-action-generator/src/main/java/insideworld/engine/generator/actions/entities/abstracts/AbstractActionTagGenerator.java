package insideworld.engine.generator.actions.entities.abstracts;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.generator.actions.entities.abstracts.info.ActionTagInfo;
import insideworld.engine.entities.tags.EntityTag;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;

public abstract class AbstractActionTagGenerator<T extends ActionTagInfo> {

    private ClassOutput output;

    public AbstractActionTagGenerator(final ClassOutput output) {
        this.output = output;
    }

    public void generate() {
        this.infos().forEach(this::generate);
    }

    private void generate(final T info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.implementation())
            .superClass(this.extended())
            .signature(this.prepareSignature(info.entity().getName()))
            .build();
        creator.addAnnotation(Singleton.class);
        this.createConstructor(creator);
        this.createKey(creator, info);
        this.createType(creator, info);
        this.methodPredicates().forEach(method -> method.accept(creator, info));
        creator.close();
    }

    protected Collection<BiConsumer<ClassCreator, T>> methodPredicates() {
        return ImmutableList.of(this::createTag);
    }

    protected abstract Class<?> extended();

    protected abstract Collection<T> infos();


    private void createConstructor(final ClassCreator creator) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>", void.class, LinksBuilder.class);
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(this.extended(),
                "<init>",
                void.class,
                LinksBuilder.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
        constructor.returnValue(null);
    }

    private void createTag(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getTag", EntityTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntityTag.class, String.class),
            method.load(info.tag())
        );
        method.returnValue(result);
    }

    private void createKey(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("key", String.class);
        method.returnValue(method.load(info.key()));
    }

    private void createType(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getType", Class.class);
        method.returnValue(method.loadClass(info.entity()));
    }

    private String prepareSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            this.extended().getName().replace(".", "/"),
            entity.replace(".", "/")
        );
    }

}
