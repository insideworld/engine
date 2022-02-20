package insideworld.engine.data.generator.jpa.storage;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.StorageInfo;
import insideworld.engine.data.generator.jpa.entity.search.JpaInfo;
import insideworld.engine.data.generator.jpa.storage.search.SearchMixin;
import insideworld.engine.data.generator.jpa.storage.search.SearchStorages;
import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.entities.Entity;
import insideworld.engine.generator.reflection.Reflection;
import io.quarkus.arc.DefaultBean;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;

public class StorageGenerator {

    private final ClassOutput output;
    private final Reflection reflection;
    private final Map<Class<? extends Entity>, JpaInfo> impls;

    public StorageGenerator(final ClassOutput output,
                            final Reflection reflection,
                            final Map<Class<? extends Entity>, JpaInfo> impls) {
        this.output = output;
        this.reflection = reflection;
        this.impls = impls;
    }

    /**
     * Default bean не ищуться CDI поэтому если мы хотим перегрузить наш сторедж то надо
     *
     */
    public void generate() {
        for (final StorageInfo info : this.findStorages()) {
            final String name = info.getImplementation();
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .className(name)
                .superClass(AbstractCrudGenericStorage.class)
                .signature(
                    this.prepareStorageSignature(
                        info.getEntity().getName(),
                        this.impls.get(info.getEntity()).getImplementation()
                    ))
                .build();
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

    private String prepareStorageSignature(final String entity, final String jpa) {
        return String.format("L%s<L%s;L%s;>;",
            AbstractCrudGenericStorage.class.getName().replace(".", "/"),
            entity.replace(".", "/"),
            jpa.replace(".", "/")
            );
    }

}
