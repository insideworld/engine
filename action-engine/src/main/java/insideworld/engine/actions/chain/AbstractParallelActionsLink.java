package insideworld.engine.actions.chain;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.threads.ThreadPool;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractParallelActionsLink implements Link {

    private final ThreadPool pool;
    private final ClassActionExecutor executor;

    public AbstractParallelActionsLink(final ThreadPool pool,
                                       final ClassActionExecutor executor) {
        this.pool = pool;
        this.executor = executor;
    }

    /**
     * @param context
     * @param output
     * @throws ActionException
     */
    @Override
    public void process(final Context context, final Output output) throws ActionException {
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
                    Pair.of(context, future.getLeft()),
                    Pair.of(output, future.getRight().get())
                    );
            } catch (final InterruptedException | ExecutionException exp) {
                this.handleException(exp, future.getLeft());
            }
        }
    }

    protected void handleResults(final Pair<Context, Context> contexts,
                                 final Pair<Output, Output> outputs) {
        outputs.getLeft().merge(outputs.getRight());
    }

    /**
     * Split context to separate data.
     *
     * @param context Context.
     * @return Splited contexts.
     */
    protected abstract Collection<Context> contexts(Context context);

    /**
     * Action which need to apply.
     *
     * @return
     */
    protected abstract Class<? extends Action> action();

    /**
     * Handle thread exception.
     *
     * @param exp
     * @param context
     * @throws ActionException
     */
    protected void handleException(final Exception exp, final Context context)
        throws ActionException {
        throw new ActionException("One of threads failed.", exp);
    }
}
