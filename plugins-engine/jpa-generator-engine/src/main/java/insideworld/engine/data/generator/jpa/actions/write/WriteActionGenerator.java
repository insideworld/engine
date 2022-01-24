package insideworld.engine.data.generator.jpa.actions.write;

import com.google.common.collect.ImmutableList;
import insideworld.engine.data.generator.jpa.actions.abstracts.AbstractActionTagGenerator;
import insideworld.engine.data.generator.jpa.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.data.generator.jpa.actions.write.search.SearchWriteAction;
import insideworld.engine.data.generator.jpa.actions.write.search.SearchWriteMixin;
import insideworld.engine.entities.actions.AbstractWriteAction;
import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class WriteActionGenerator extends AbstractActionTagGenerator<ActionTagInfo> {

    private Reflection reflection;

    public WriteActionGenerator(
        final Reflection reflection,
        final ClassOutput output,
        final String packages) {
        super(output, packages + ".Write%sAction");
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
