/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.depracted;

import com.google.common.collect.Maps;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.ChainTags;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.executor.ClassActionExecutor;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.tags.ActionsTags;
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

    /**
     * Some.
     */
    private final ClassActionExecutor executor;

    /**
     * Some.
     * @param executor Some.
     */
    @Inject
    public BulkHandlingLink(final ClassActionExecutor executor) {
        this.executor = executor;
    }

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        if (!context.contains(ChainTags.BULK)) {
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
            final Context clone = context.cloneContext(record);
            output.merge(this.executor.execute(action.getClass(), clone));
        }
        context.put(ChainTags.BREAK_CHAIN, new Object());
    }
}
