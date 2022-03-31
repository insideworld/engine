package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Execute before process child action.
 * @see ExecuteActionLink
 * @since 0.6.0
 */
public interface PreExecute {

    /**
     * Apply some addtional action before execute action.
     * @param context
     * @return Skip if false
     */
    boolean apply(Pair<Context, Context> contexts);

}
