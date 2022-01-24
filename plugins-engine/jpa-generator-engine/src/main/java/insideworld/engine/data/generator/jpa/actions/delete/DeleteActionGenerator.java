package insideworld.engine.data.generator.jpa.actions.delete;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.actions.abstracts.AbstractActionTagsGenerator;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfo;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagsInfoImpl;
import insideworld.engine.data.generator.jpa.actions.delete.search.SearchDeleteAction;
import insideworld.engine.data.generator.jpa.actions.delete.search.SearchDeleteMixin;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchReadActionMixin;
import insideworld.engine.data.generator.jpa.actions.read.search.SearchReadAction;
import insideworld.engine.entities.actions.AbstractDeleteAction;
import insideworld.engine.entities.actions.AbstractReadAction;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class DeleteActionGenerator extends AbstractActionTagsGenerator<ActionTagsInfo> {

    private Reflection reflection;

    public DeleteActionGenerator(final Reflection reflection,
                                 final ClassOutput output,
                                 final String packages) {
        super(output, packages + ".Delete%sAction");
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
