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

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.executor.ClassActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.threads.ThreadPool;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Deprecated.
 * @since 0.1.0
 */
public abstract class AbstractParallelActionsLink implements Link {

    /**
     * Deprecated.
     */
    private final ThreadPool pool;

    /**
     * Deprecated.
     */
    private final ClassActionExecutor executor;

    /**
     * Deprecated.
     * @param pool Deprecated.
     * @param executor Deprecated.
     */
    public AbstractParallelActionsLink(
        final ThreadPool pool,
        final ClassActionExecutor executor) {
        this.pool = pool;
        this.executor = executor;
    }

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        final Collection<Context> contexts = this.contexts(context);
        final Collection<Pair<Context, Future<Output>>> futures =
            Lists.newArrayListWithCapacity(contexts.size());
        final Class<? extends Action> action = this.action();
        for (final Context clone : contexts) {
            final Future<Output> future = this.pool.execute(
                () -> this.executor.execute(action, clone)
            );
            futures.add(Pair.of(clone, future));
        }
        for (final Pair<Context, Future<Output>> future : futures) {
            try {
                this.handleResults(
                    Pair.of(context, future.getLeft()), Pair.of(output, future.getRight().get())
                );
            } catch (final InterruptedException | ExecutionException exp) {
                this.handleException(exp, future.getLeft());
            }
        }
    }

    /**
     * Deprecated.
     * @param contexts Qwe.
     * @param outputs Qwe.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    protected void handleResults(
        final Pair<Context, Context> contexts,
        final Pair<Output, Output> outputs) {
        outputs.getLeft().merge(outputs.getRight());
    }

    /**
     * Split context to separate data.
     * @param context Context.
     * @return Splited contexts.
     */
    protected abstract Collection<Context> contexts(Context context);

    /**
     * Action which need to apply.
     * @return Qwe.
     */
    protected abstract Class<? extends Action> action();

    /**
     * Handle thread exception.
     * @param exp Qwe.
     * @param context Qwe.
     * @throws ActionException Qwe.
     */
    protected void handleException(final Exception exp, final Context context)
        throws ActionException {
        throw new ActionException("One of threads failed.", exp);
    }
}
