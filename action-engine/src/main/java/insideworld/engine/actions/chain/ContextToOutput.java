package insideworld.engine.actions.chain;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.context.Dependent;

@Dependent
public class ContextToOutput implements Link {

    private final Collection<String> tags = Lists.newLinkedList();

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (tags.isEmpty()) {
            return;
        }
        final Record record = output.createRecord();
        for (final String tag : this.tags) {
            if (context.contains(tag)) {
                record.put(tag, context.get(tag));
            }
        }
    }

    public ContextToOutput addTag(final Tag<?> tag) {
        this.tags.add(tag.getTag());
        return this;
    }

    public ContextToOutput addTags(final Tag<?>... tags) {
        Arrays.stream(tags).forEach(tag -> this.tags.add(tag.getTag()));
        return this;
    }

    public ContextToOutput addTag(final String tag) {
        this.tags.add(tag);
        return this;
    }

    public ContextToOutput addTags(final String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

}
