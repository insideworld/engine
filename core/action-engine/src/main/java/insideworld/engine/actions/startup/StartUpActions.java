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

package insideworld.engine.actions.startup;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.OnStartupAction;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Execute some actions after startup.
 * Necessary action should contain annotation OnStartupAction.
 * @see OnStartupAction
 * @since 0.14.0
 */
@Singleton
public class StartUpActions implements OnStartUp {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Collection of all actions.
     */
    private final Collection<Action> actions;

    /**
     * Default constructor.
     * @param executor Class action executor.
     * @param actions Actions.
     */
    @Inject
    public StartUpActions(
        final ActionExecutor<Class<? extends Action>> executor,
        final List<Action> actions) {
        this.executor = executor;
        this.actions = actions;
    }

    @Override
    public final void startUp() throws StartUpException {
        for (final Action action : this.actions) {
            if (action.getClass().isAnnotationPresent(OnStartupAction.class)) {
                try {
                    this.executor.execute(
                        action.getClass(),
                        this.executor.createContext(),
                        SystemExecuteProfile.class
                    );
                } catch (final ActionException exp) {
                    throw new StartUpException(exp);
                }
            }
        }
    }

    @Override
    public final long startOrder() {
        return 200_000;
    }
}
