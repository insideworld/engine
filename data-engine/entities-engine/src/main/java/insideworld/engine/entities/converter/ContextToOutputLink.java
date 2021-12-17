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

@Dependent
public class ContextToOutputLink implements Link {

    private final Collection<Tag<?>> tags = Lists.newLinkedList();

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (tags.isEmpty()) {
            return;
        }
        final Record record = output.createRecord();
        for (final Tag tag : this.tags) {
            if (context.contains(tag)) {
                record.put(tag, context.get(tag));
            }
        }
    }

    public ContextToOutputLink addTag(final Tag<?> tag) {
        this.tags.add(tag);
        return this;
    }
}
