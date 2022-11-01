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
import insideworld.engine.actions.executor.ActionChanger;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup(3000)
@Singleton
public class ActionsInit {

    private final Collection<Action> actions;
    private final Collection<ActionChanger> changers;
    private final ObjectFactory factory;

    @Inject
    public ActionsInit(final List<Action> actions,
                       final Collection<ActionChanger> changers,
                       final ObjectFactory factory) {
        this.actions = actions;
        this.changers = changers;
        this.factory = factory;
    }

    @PostConstruct
    public void init() throws ActionException {
        for (final ActionChanger changer : this.changers) {
            changer.addActions(this.actions);
        }
    }
}
