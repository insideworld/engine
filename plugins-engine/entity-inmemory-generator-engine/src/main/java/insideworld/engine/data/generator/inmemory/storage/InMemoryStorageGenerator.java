package insideworld.engine.data.generator.inmemory.storage;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.data.generator.StorageInfo;
import insideworld.engine.data.generator.inmemory.storage.abstracts.AbstractMemoryStorage;
import insideworld.engine.data.generator.inmemory.storage.search.SearchMixin;
import insideworld.engine.data.generator.inmemory.storage.search.SearchStorages;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.reflection.Reflection;
import io.quarkus.arc.DefaultBean;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

public class InMemoryStorageGenerator {

    private final ClassOutput output;
    private final Reflection reflection;

    public InMemoryStorageGenerator(final ClassOutput output,
                                    final Reflection reflection) {
        this.output = output;
        this.reflection = reflection;
    }

    public void generate() {
        for (final StorageInfo info : this.findStorages()) {
            final String name = info.getImplementation();
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .className(name)
                .superClass(AbstractMemoryStorage.class)
                .signature(this.prepareStorageSignature(info.getEntity().getName()))
                .build();
            this.createConstructor(creator);
            creator.addAnnotation(Singleton.class);
            if (info.isOverride()) {
                creator.addAnnotation(DefaultBean.class);
            }
            creator.close();
        }
    }

    private Collection<StorageInfo> findStorages() {
        final Collection<SearchStorages> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchStorages::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private String prepareStorageSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            AbstractMemoryStorage.class.getName().replace(".", "/"),
            entity.replace(".", "/")
            );
    }

    private void createConstructor(final ClassCreator creator) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>", void.class, ObjectFactory.class);
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(AbstractMemoryStorage.class,
                "<init>",
                void.class,
                ObjectFactory.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
        constructor.returnValue(null);
    }

}
