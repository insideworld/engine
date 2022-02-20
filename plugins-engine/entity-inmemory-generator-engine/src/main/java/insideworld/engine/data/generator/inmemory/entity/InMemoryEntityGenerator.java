package insideworld.engine.data.generator.inmemory.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.inmemory.entity.search.InMemoryInfo;
import insideworld.engine.data.generator.inmemory.entity.search.SearchEntities;
import insideworld.engine.data.generator.inmemory.entity.search.SearchMixin;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class InMemoryEntityGenerator {

    private final ClassOutput output;
    private final Reflection reflection;

    public InMemoryEntityGenerator(final ClassOutput output,
                                   final Reflection reflection) {
        this.output = output;
        this.reflection = reflection;
    }

    public void generate() {
        final InMemoryEntityClassGenerator entity = new InMemoryEntityClassGenerator(this.output);
        final InMemoryEntityFieldsGenerator fields = new InMemoryEntityFieldsGenerator();
        for (final InMemoryInfo info : this.findInfos()) {
            try {
                final ClassCreator creator = entity.createEntity(info);
                fields.createFields(creator, info);
                creator.close();
            } catch (final Exception exp) {
                throw new RuntimeException(
                    "Can't create entity for type" + info.getEntity().getName(),
                    exp
                );
            }
        }
    }

    private Collection<InMemoryInfo> findInfos() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
