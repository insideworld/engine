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

package insideworld.engine.actions.executor.profiles;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.startup.OnStartUp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Default execute profile.
 * Using by default to execute an action.
 * On start up will create a chain.
 * @since 0.1.0
 */
@Singleton
public class DefaultExecuteProfile implements ExecuteProfile, OnStartUp {

    /**
     * Wrappers collection.
     */
    private final List<ExecuteWrapper> executors;

    /**
     * Collection of PreExecutor.
     */
    private ExecuteWrapper first;

    /**
     * Default constructor.
     * Sort executors and filter by profile.
     * @param executors All executors in the system.
     */
    @Inject
    public DefaultExecuteProfile(final List<ExecuteWrapper> executors) {
        this.executors = executors.stream()
            .filter(executor -> executor.forProfile().contains(DefaultExecuteProfile.class))
            .sorted(Comparator.comparingInt(ExecuteWrapper::order).reversed())
            .toList();
    }

    @Override
    public final void execute(final Action action, final Context context, final Output output)
        throws ActionException {
        this.first.execute(action, context, output);
    }

    @Override
    public final void startUp() {
        final Iterator<ExecuteWrapper> iterator = this.executors.iterator();
        ExecuteWrapper last = iterator.next();
        this.first = last;
        while (iterator.hasNext()) {
            final ExecuteWrapper next = iterator.next();
            last.setNext(next);
            last = next;
        }
    }

    @Override
    public final int order() {
        return 20_000;
    }
}
