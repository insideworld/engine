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

package insideworld.engine.actions.quarkus.beans;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.facade.OnStartupAction;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.facade.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup(3500)
@Singleton
public class LaunchStartupAction {

    private final ClassActionExecutor executor;
    private final Collection<Action> actions;
    private final ObjectFactory factory;

    @Inject
    public LaunchStartupAction(final ClassActionExecutor executor,
                               final Collection<Action> actions,
                               final ObjectFactory factory) {
        this.executor = executor;
        this.actions = actions;
        this.factory = factory;
    }

    @PostConstruct
    public void init() throws ActionException {
        for (final Action action : this.actions) {
            if (action.getClass().isAnnotationPresent(OnStartupAction.class)) {
                this.executor.execute(
                    action.getClass(),
                    this.factory.createObject(Context.class),
                    SystemExecuteProfile.class
                );
            }
        }
    }
}
