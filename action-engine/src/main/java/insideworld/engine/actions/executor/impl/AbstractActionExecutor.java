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

package insideworld.engine.actions.executor.impl;

import com.google.common.collect.Maps;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.actions.executor.ActionChanger;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.profiles.DefaultExecuteProfile;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.tags.ActionsTags;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to execute different actions.
 * Need to extend from this class to support execute action from specific key.
 *
 * @param <T> Key of action.
 * @since 0.0.1
 */
public abstract class AbstractActionExecutor<T> implements ActionExecutor<T>, ActionChanger {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractActionExecutor.class);

    /**
     * Map of all actions in the system.
     */
    private final Map<T, Action> actions;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * All ExecuteProfiles in the system.
     */
    private final Map<Class<? extends ExecuteProfile>, ExecuteProfile> profiles;

    /**
     * Default constructor.
     *
     * @param factory Object factory.
     * @param profiles Collection of all executor profiles.
     */
    public AbstractActionExecutor(
        final ObjectFactory factory,
        final Collection<ExecuteProfile> profiles) {
        this.factory = factory;
        this.profiles = profiles.stream().collect(
            Collectors.toMap(ExecuteProfile::getClass, Function.identity())
        );
        this.actions = Maps.newHashMap();
    }

    @Override
    public final Output execute(final T parameter, final Context context)
        throws ActionRuntimeException {
        return this.execute(parameter, context, DefaultExecuteProfile.class);
    }

    @Override
    public final Output execute(
        final T parameter, final Context context, final Class<? extends ExecuteProfile> profile)
        throws ActionRuntimeException {
        final Output output;
        if (context.contains(ActionsTags.USE_EXIST_TX)) {
            output = this.executeSameTx(parameter, context, profile);
        } else {
            output = this.executeNewTx(parameter, context, profile);
        }
        return output;
    }

    @Override
    public final Context createContext() {
        return this.factory.createObject(Context.class);
    }

    @Override
    public final void addAction(final Action action) {
        this.actions.put(this.defineKey(action), action);
    }

    @Override
    public final void addActions(final Collection<Action> pactions) {
        pactions.forEach(this::addAction);
    }

    /**
     * Execute action in the same TX.
     * Use JTA transaction annotation.
     * This method is public but should be private. JTA is not support private method.
     *
     * @param parameter Action key.
     * @param context Context.
     * @param profile Executor profile.
     * @return Output.
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public final Output executeSameTx(
        final T parameter, final Context context, final Class<? extends ExecuteProfile> profile) {
        return this.executeInternal(parameter, context, profile);
    }

    /**
     * Execute action in the new TX.
     * Use JTA transaction annotation.
     * This method is public but should be private. JTA is not support private method.
     *
     * @param parameter Action key.
     * @param context Context.
     * @param profile Executor profile.
     * @return Output.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public final Output executeNewTx(
        final T parameter, final Context context, final Class<? extends ExecuteProfile> profile) {
        return this.executeInternal(parameter, context, profile);
    }

    /**
     * Abstract method to define key of each action.
     *
     * @param action Action,
     * @return Key of action.
     */
    protected abstract T defineKey(Action action);

    /**
     * Execute action.
     * This method internal and using for common point for executeNewTx and executeSameTx methods.
     *
     * @param parameter Action key.
     * @param context Context.
     * @param profile Executor profile.
     * @return Output.
     */
    private Output executeInternal(
        final T parameter, final Context context, final Class<? extends ExecuteProfile> profile) {
        final Action action = this.provide(parameter);
        LOGGER.info("Start action {} with key {}", action.getClass().getSimpleName(), action.key());
        context.put(ActionsTags.ACTION, action);
        try {
            this.profiles.get(profile).preExecute(context);
            final var output = this.factory.createObject(Output.class);
            action.execute(context, output);
            return output;
        } catch (final ActionException exp) {
            throw new ActionRuntimeException(exp);
        }
    }

    /**
     * Find execution action by key.
     *
     * @param parameter Action key.
     * @return Action bound to the key.
     */
    private Action provide(final T parameter) {
        final var action = this.actions.get(parameter);
        Validate.notNull(action, "Can't find an action with parameter: %s", parameter);
        return action;
    }
}
