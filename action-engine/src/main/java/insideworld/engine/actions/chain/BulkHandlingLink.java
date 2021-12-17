package insideworld.engine.actions.chain;

import com.google.common.collect.Maps;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Execute the same instance for bulk cases with different actions.
 *
 * @since 0.5.0
 */
@Singleton
public class BulkHandlingLink implements Link {

    private final ClassActionExecutor executor;

    @Inject
    public BulkHandlingLink(final ClassActionExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (!context.contains(ActionsTags.BULK)) {
            return;
        }
        final Map<Integer, Record> records = Maps.newTreeMap();
        for (final var entry : context.values().entrySet()) {
            if (!(entry.getValue() instanceof Record)) {
                continue;
            }
            records.put(Integer.valueOf(entry.getKey()), (Record) entry.getValue());
        }
        final Action action = context.get(ActionsTags.ACTION);
        for (final Record record : records.values()) {
            final Context clone = context.clone(record);
            output.merge(this.executor.execute(action.getClass(), clone));
        }
        context.put(ActionsTags.BREAK_CHAIN, new Object());
    }
}
