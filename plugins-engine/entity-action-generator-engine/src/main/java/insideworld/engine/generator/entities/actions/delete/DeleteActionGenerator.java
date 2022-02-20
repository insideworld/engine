package insideworld.engine.generator.entities.actions.delete;

import com.google.common.collect.ImmutableList;
import insideworld.engine.entities.actions.AbstractDeleteAction;
import insideworld.engine.generator.entities.actions.abstracts.AbstractActionTagsGenerator;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.generator.entities.actions.delete.search.SearchDeleteAction;
import insideworld.engine.generator.entities.actions.delete.search.SearchDeleteMixin;
import insideworld.engine.generator.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class DeleteActionGenerator extends AbstractActionTagsGenerator<ActionTagsInfo> {

    private final Reflection reflection;

    public DeleteActionGenerator(final Reflection reflection,
                                 final ClassOutput output) {
        super(output);
        this.reflection = reflection;
    }

    @Override
    protected Class<?> extended() {
        return AbstractDeleteAction.class;
    }

    @Override
    protected Collection<ActionTagsInfo> infos() {
        final Collection<SearchDeleteAction> searchers = ImmutableList.of(
            new SearchDeleteMixin(this.reflection)
        );
        return searchers.stream().map(SearchDeleteAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
