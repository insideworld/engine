package insideworld.engine.entities.converter;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Collection;
import javax.enterprise.context.Dependent;

/**
 * Check all context and remove present tags from output.
 *
 * @since 0.0.1
 */
@Dependent
public class OutputRemoveLink implements Link {

    private final Collection<String> remove = Lists.newLinkedList();

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        for (final Record record : output) {
            record.values().keySet().removeAll(remove);
        }
    }

    public OutputRemoveLink add(final Tag<?> tag) {
        this.remove.add(tag.getTag());
        return this;
    }
}
