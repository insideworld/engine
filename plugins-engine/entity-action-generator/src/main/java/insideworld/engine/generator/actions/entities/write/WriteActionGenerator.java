package insideworld.engine.generator.actions.entities.write;

import com.google.common.collect.ImmutableList;
import insideworld.engine.generator.actions.entities.abstracts.AbstractActionTagGenerator;
import insideworld.engine.generator.actions.entities.abstracts.info.ActionTagInfo;
import insideworld.engine.generator.actions.entities.write.search.SearchWriteAction;
import insideworld.engine.generator.actions.entities.write.search.SearchWriteMixin;
import insideworld.engine.entities.actions.AbstractWriteAction;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class WriteActionGenerator extends AbstractActionTagGenerator<ActionTagInfo> {

    private Reflection reflection;

    public WriteActionGenerator(final Reflection reflection, final ClassOutput output) {
        super(output);
        this.reflection = reflection;
    }

    @Override
    protected Class<?> extended() {
        return AbstractWriteAction.class;
    }

    @Override
    protected Collection<ActionTagInfo> infos() {
        final Collection<SearchWriteAction> searchers = ImmutableList.of(
            new SearchWriteMixin(this.reflection)
        );
        return searchers.stream().map(SearchWriteAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
