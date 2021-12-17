package insideworld.engine.actions;


import com.fasterxml.jackson.annotation.JsonGetter;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
/**
 *
 * @since 0.0.1
 */
public interface Action {

    /**
     * Execute an action.
     * @param context Input data.
     * @return Actions.
     */
    void execute(Context context, Output output) throws ActionException;


    /**
     * Key of action in string representation.
     * @return Key.
     */
    @JsonGetter
    String key();
}
