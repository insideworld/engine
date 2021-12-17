package insideworld.engine.actions.facade;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;


/**
 * Using for launch action.
 *
 * @param <T> Type of action selector.
 * @since 0.0.1
 */
public interface ActionExecutor<T> {

    /**
     * Execute specific action by parameter.
     * @param parameter Parameter for define an action.
     * @param context Input context.
     * @return Result of action.
     * @throws ActionException Something went wrong.
     */
    Output execute(T parameter, Context context) throws ActionRuntimeException;

    /**
     * Execute action in internal mechanism like jobs, preinit and etc.
     * @param parameter Action parameter.
     * @param context Context parameter.
     * @param profile Execute with specific profile.
     * @return
     * @throws ActionRuntimeException
     */
    Output execute(T parameter, Context context, Class<? extends ExecuteProfile> profile)
        throws ActionRuntimeException;

    /**
     * Create a context.
     * @return Context instance.
     */
    Context createContext();

}
