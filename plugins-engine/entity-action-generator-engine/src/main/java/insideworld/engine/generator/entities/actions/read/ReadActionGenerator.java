package insideworld.engine.generator.entities.actions.read;

import com.google.common.collect.ImmutableList;
import insideworld.engine.generator.entities.actions.abstracts.AbstractActionTagsGenerator;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.generator.entities.actions.read.search.SearchReadActionMixin;
import insideworld.engine.generator.entities.actions.read.search.SearchReadAction;
import insideworld.engine.entities.actions.AbstractReadAction;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class ReadActionGenerator extends AbstractActionTagsGenerator<ActionTagsInfo> {

    private final Reflection reflection;

    public ReadActionGenerator(final Reflection reflection,
                               final ClassOutput output) {
        super(output);
        this.reflection = reflection;
    }

    @Override
    protected Class<?> extended() {
        return AbstractReadAction.class;
    }

    @Override
    protected Collection<ActionTagsInfo> infos() {
        final Collection<SearchReadAction> searchers = ImmutableList.of(
            new SearchReadActionMixin(this.reflection)
        );
        return searchers.stream().map(SearchReadAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
