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
import insideworld.engine.actions.executor.ActionChanger;
import insideworld.engine.exception.CommonException;
import insideworld.engine.startup.OnStartUp;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Startup class.
 * Using to fill all actions in the system.
 *
 * @since 0.14.0
 */
@Singleton
public class ActionsInit implements OnStartUp {

    /**
     * Collection of actions.
     */
    private final Collection<Action> actions;

    /**
     * Collection of action changers.
     */
    private final Collection<ActionChanger> changers;

    /**
     * Default constructor.
     *
     * @param actions List of actions.
     * @param changers List of changers.
     */
    @Inject
    public ActionsInit(final List<Action> actions, final List<ActionChanger> changers) {
        this.actions = actions;
        this.changers = changers;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public final void startUp() throws ActionException {
        for (final Action action : this.actions) {
            //@checkstyle IllegalCatchCheck (10 lines)
            try {
                action.init();
            } catch (final Exception exp) {
                throw CommonException.wrap(
                    exp,
                    () -> new ActionException(action, exp),
                    ActionException.class
                );
            }
        }
        for (final ActionChanger changer : this.changers) {
            changer.addActions(this.actions);
        }
    }

    @Override
    public final long startOrder() {
        return 800_000;
    }
}
