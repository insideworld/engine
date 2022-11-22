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

package insideworld.engine.datatransfer.endpoint.actions;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskPredicate;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

/**
 * Create action receiver.
 */
public abstract class AbstractActionReceiver<T> implements ActionReceiver<T> {

    private final ActionExecutor<String> executor;
    private final OutputTask task;

    @Inject
    public AbstractActionReceiver(
        final ActionExecutor<String> executor,
        final OutputTask task
    ) {
        this.executor = executor;
        this.task = task;
    }

    public final Task<Output> execute(
        final String action,
        final Class<? extends ExecuteProfile> profile,
        final Collection<T> inputs)
    {
        final List<TaskPredicate<Output>> predicates = inputs.stream().map(
            input -> (TaskPredicate<Output>)
                () -> executeAction(action, profile, convert(input))
        ).toList();
        return this.task.createTask(predicates);
    }

    protected abstract Context convert(T input);

    private Output executeAction(final String action,
                                 final Class<? extends ExecuteProfile> profile,
                                 final Context context) {
        try {
            return this.executor.execute(action, context, profile);
        } catch (ActionException e) {
            throw new RuntimeException(e);
        }
    }
}
