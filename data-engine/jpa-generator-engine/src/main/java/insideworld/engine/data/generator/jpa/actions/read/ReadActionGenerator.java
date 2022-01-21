package insideworld.engine.data.generator.jpa.actions.read;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchMixin;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchReadAction;
import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.entities.actions.AbstractReadAction;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

public class ReadActionGenerator {

    private final Reflection reflection;
    private final ClassOutput output;
    private final String packages;

    public ReadActionGenerator(
        final Reflection reflection, final ClassOutput output, final String packages) {
        this.reflection = reflection;
        this.output = output;
        this.packages = packages + ".generated.actions.Read%sAction";
    }

    public void generate() {
        this.findInfos().forEach(this::generate);
    }

    private void generate(final GenerateReadAction info) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .className(this.name(info.entity()))
            .superClass(AbstractReadAction.class)
            .signature(this.prepareSignature(info.entity().getName()))
            .build();
        creator.addAnnotation(Singleton.class);
        this.createConstructor(creator);
        this.createTag(creator, info);
        this.createTags(creator, info);
        this.createType(creator, info);
        this.createKey(creator, info);
        creator.close();
    }

    private void createConstructor(final ClassCreator creator) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>", void.class, LinksBuilder.class);
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(AbstractReadAction.class,
                "<init>",
                void.class,
                LinksBuilder.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
    }

    private void createTag(final ClassCreator creator, final GenerateReadAction info) {
        final MethodCreator method = creator.getMethodCreator("getTag", EntityTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntityTag.class, String.class),
            method.load(info.tag())
        );
        method.returnValue(result);
    }

    private void createTags(final ClassCreator creator, final GenerateReadAction info) {
        final MethodCreator method = creator.getMethodCreator("getTags", EntitiesTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntitiesTag.class, String.class),
            method.load(info.tags())
        );
        method.returnValue(result);
    }

    private void createType(final ClassCreator creator, final GenerateReadAction info) {
        final MethodCreator method = creator.getMethodCreator("getType", Class.class);
        method.returnValue(method.loadClass(info.entity()));
    }

    private void createKey(final ClassCreator creator, final GenerateReadAction info) {
        final MethodCreator method = creator.getMethodCreator("key", String.class);
        method.returnValue(method.load(info.key()));
    }


    private Collection<GenerateReadAction> findInfos() {
        final Collection<SearchReadAction> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchReadAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private String name(final Class<?> entity) {
        return String.format(this.packages,
            entity.getSimpleName().replace("Entity",""));
    }

    private String prepareSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            AbstractReadAction.class.getName().replace(".", "/"),
            entity.replace(".", "/")
        );
    }
}
