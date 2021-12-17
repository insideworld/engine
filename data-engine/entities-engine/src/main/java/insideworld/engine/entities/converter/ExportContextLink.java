package insideworld.engine.entities.converter;

import com.google.common.collect.ImmutableSet;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Collection;
import javax.enterprise.context.Dependent;

/**
 * Link usign to export specific tags from context to output.
 *
 * You need to set in #addTags tags which you want to export in output.
 * Support only single output.
 *
 * @since 0.0.6
 */
@Dependent
public class ExportContextLink implements Link {

    /**
     * Collection of tags which need to export.
     */
    private Collection<Tag<?>> tags;

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.tags != null) {
            final Record record = output.createRecord();
            for (final Tag<?> tag : this.tags) {
                record.put(tag.getTag(), context.get(tag));
            }
        }
    }

    /**
     * Add export tags.
     * @param tags Tags which need to propagate in output.
     */
    public void addTags(final Collection<Tag<?>> tags) {
        this.tags = ImmutableSet.copyOf(tags);
    }

}
