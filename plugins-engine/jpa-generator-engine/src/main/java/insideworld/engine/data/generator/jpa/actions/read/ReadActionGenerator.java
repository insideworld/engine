package insideworld.engine.data.generator.jpa.actions.read;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.actions.abstracts.AbstractActionTagsGenerator;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchReadActionMixin;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchReadAction;
import insideworld.engine.entities.actions.AbstractReadAction;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class ReadActionGenerator extends AbstractActionTagsGenerator<ActionTagsInfo> {

    private Reflection reflection;

    public ReadActionGenerator(final Reflection reflection,
                               final ClassOutput output,
                               final String packages) {
        super(output, packages + ".Read%sAction");
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
