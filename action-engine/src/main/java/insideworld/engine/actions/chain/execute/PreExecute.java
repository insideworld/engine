package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;

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
    boolean apply(Context context);

}
