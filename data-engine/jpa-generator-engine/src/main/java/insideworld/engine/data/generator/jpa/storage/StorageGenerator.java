package insideworld.engine.data.generator.jpa.storage;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.storage.search.SearchMixin;
import insideworld.engine.data.generator.jpa.storage.search.SearchStorages;
import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.entities.Entity;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;

public class StorageGenerator {

    private final ClassOutput output;
    private final Reflection reflection;
    private final String packages;
    private final Map<Class<? extends Entity>, String> impls;

    public StorageGenerator(final ClassOutput output,
                            final Reflection reflection,
                            final String packages,
                            final Map<Class<? extends Entity>, String> impls) {
        this.output = output;
        this.reflection = reflection;
        this.packages = packages + ".generated.crud.%sStorage";
        this.impls = impls;
    }

    public void generate() {
        for (final Class<? extends Entity> entity : this.findStorages()) {
            final String name = this.generateName(entity);
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(output)
                .className(name)
                .superClass(AbstractCrudGenericStorage.class)
                .signature(this.prepareStorageSignature(entity.getName(), this.impls.get(entity)))
                .build();
            creator.addAnnotation(Singleton.class);
            creator.close();
        }

    }

    private String generateName(final Class<? extends Entity> entity) {
        return String.format(this.packages,
            entity.getSimpleName().replace("Entity",""));
    }

    private Collection<Class<? extends Entity>> findStorages() {
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
