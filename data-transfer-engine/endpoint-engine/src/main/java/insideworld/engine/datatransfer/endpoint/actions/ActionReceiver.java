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

import com.google.common.collect.Lists;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.threads.AsyncResult;
import insideworld.engine.threads.TaskBuilder;
import insideworld.engine.threads.ThreadPool;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Create action receiver.
 */
@Dependent
public class ActionReceiver<P, T extends ThreadPool> {

    private final ActionExecutor<String> executor;
    private final ObjectFactory factory;

    @Inject
    public ActionReceiver(
        final ActionExecutor<String> executor,
        final ObjectFactory factory
    ) {
        this.executor = executor;
        this.factory = factory;
    }

    public final Output execute(
        final String action,
        final P parameter,
        final Map<String, Object> map
    ) {
        


    }

    public final Output executeSingle()
        throws CommonException {
        final Context context = this.executor.createContext();
        map.forEach(context::put);
        this.processPreExecute(context, parameter);
        return this.executor.execute(action, context);
    }

    public final Output executeMulti(final String action,
                                     final P parameter,
                                     final Map<String, Object>... maps)
        throws CommonException {
        final Output output = this.factory.createObject(Output.class);
        final Collection<AsyncResult<Output>> results =
            Lists.newArrayListWithCapacity(maps.length);
        for (final Map<String, Object> map : maps) {
            final AsyncResult<Output> result = this.pool.execute(
                () -> this.executeSingle(action, parameter, map)
            );
            results.add(result);
        }
        for (final AsyncResult<Output> result : results) {
            output.merge(result.get());
        }
        return output;
    }

    private void processPreExecute(final Context context, final P parameter)
        throws CommonException {
        for (final PreExecute<P> pre : this.pres) {
            pre.preExecute(context, parameter);
        }
    }
}
