package insideworld.engine.actions.chain;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;


/**
 * Link of chain. Can be used for attach some code to action.
 *
 * @since 0.0.1
 */
public interface Link {

    /**
     * Process some logic.
     * @param data
     */
    void process(Context context, Output output) throws ActionException;

}
