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

package insideworld.engine.datatransfer.endpoint.actions.facade;

import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.datatransfer.endpoint.actions.OutputTaskBuilder;
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskPredicate;
import java.util.Collection;
import java.util.List;

/**
 * Provide functionality to execute an action with specific profiler and contexts consumers.
 * @since 0.14.0
 */
public abstract class AbstractActionFacade<T> implements ActionFacade<T> {

    private final OutputTaskBuilder builder;
    private final ActionExecutor<String> executor;
    private final Class<? extends ExecuteProfile> profile;

    public AbstractActionFacade(
        final OutputTaskBuilder builder,
        final ActionExecutor<String> executor,
        final Class<? extends ExecuteProfile> profile
    ) {
        this.builder = builder;
        this.executor = executor;
        this.profile = profile;
    }

    public final Task<Output> execute(
        final String action,
        final T parameter
    ) {
        final List<TaskPredicate<Output>> predicates = this.contexts(parameter).stream().map(
            context -> (TaskPredicate<Output>)
                () -> this.executor.execute(
                    action,
                    context.initContext(),
                    this.profile
                )
        ).toList();
        return this.builder.createTask(predicates);
    }

    protected final Context createContext() {
        return this.executor.createContext();
    }

    protected abstract Collection<ContextPredicate> contexts(T parameter);

}
