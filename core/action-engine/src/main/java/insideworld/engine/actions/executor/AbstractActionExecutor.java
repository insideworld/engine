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

package insideworld.engine.actions.executor;

import com.google.common.collect.Maps;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.profiles.DefaultExecuteProfile;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.tags.ActionsTags;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class to execute different actions.
 * Need to extend from this class to support execute action from specific key.
 * The executed action will execute in process wrappers chain in default profile.
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
        final List<ExecuteProfile> profiles) {
        this.factory = factory;
        this.profiles = profiles.stream().collect(
            Collectors.toMap(ExecuteProfile::getClass, Function.identity())
        );
        this.actions = Maps.newHashMap();
    }

    @Override
    public final Output execute(final T parameter, final Context context)
        throws ActionException {
        return this.execute(parameter, context, DefaultExecuteProfile.class);
    }

    @Override
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})
    public final Output execute(
        final T parameter, final Context context, final Class<? extends ExecuteProfile> profile)
        throws ActionException {
        final Action action = this.provide(parameter);
        LOGGER.info("Start action {} with key {}", action.getClass().getSimpleName(), action.key());
        context.put(ActionsTags.ACTION, action);
        context.put(ActionsTags.PROFILE, profile);
        final var output = this.factory.createObject(Output.class);
        //@checkstyle IllegalCatchCheck (7 lines)
        try {
            this.profiles.get(profile).execute(action, context, output);
        } catch (final Exception exp) {
            throw CommonException.wrap(
                exp,
                () -> new ActionException(action, exp),
                ActionException.class
            );
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
     * Abstract method to define key of each action.
     *
     * @param action Action,
     * @return Key of action.
     */
    protected abstract T defineKey(Action action);

    /**
     * Find execution action by key.
     *
     * @param parameter Action key.
     * @return Action bound to the key.
     * @throws ActionException Can't find action.
     */
    private Action provide(final T parameter) throws ActionException {
        final var action = this.actions.get(parameter);
        if (action == null) {
            throw new ActionException(
                new NotFoundAction(),
                "Can't find an action with parameter: %s",
                parameter
            );
        }
        return action;
    }
}
