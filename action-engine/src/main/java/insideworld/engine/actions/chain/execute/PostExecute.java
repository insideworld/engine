package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;

/**
 * Execute after process a child action.
 * @see ExecuteActionLink
 * @since 0.6.0
 */
public interface PostExecute {

    /**
     * Apply some additional actions.
     * @param context Child context.
     * @param output Child output.
     * @return In case if true - output will merge with parent output. False - won't merge
     */
    boolean apply(Context context, Output output);

}
