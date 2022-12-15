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

package insideworld.engine.core.action.startup;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.OnStartupAction;
import insideworld.engine.core.common.startup.OnStartUp;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Execute some actions after startup.
 * Necessary action should contain annotation OnStartupAction.
 *
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
     *
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
    public final void startUp() throws ActionException {
        for (final Action action : this.actions) {
            if (action.getClass().isAnnotationPresent(OnStartupAction.class)) {
                this.executor.execute(action.getClass(), this.executor.createContext());
            }
        }
    }

    @Override
    public final long startOrder() {
        return 200_000;
    }
}