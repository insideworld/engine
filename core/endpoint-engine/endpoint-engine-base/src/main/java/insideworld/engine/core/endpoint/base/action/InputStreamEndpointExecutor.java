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

package insideworld.engine.core.endpoint.base.action;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.Input;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.endpoint.base.action.serializer.ActionSerializer;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.threads.Task;
import insideworld.engine.core.common.threads.TaskBuilder;
import java.io.InputStream;
import java.util.function.Consumer;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InputStreamEndpointExecutor implements EndpointExecutor<InputStream> {

    private final ActionExecutor executor;
    private final ActionSerializer serializer;
    private final ObjectFactory factory;

    @Inject
    public InputStreamEndpointExecutor(
        final ActionExecutor executor,
        final ActionSerializer serializer,
        final ObjectFactory factory
    ) {
        this.executor = executor;
        this.serializer = serializer;
        this.factory = factory;
    }

    @Override
    public <I, O> O execute(
        final Key<I, O> key,
        final InputStream stream,
        final Consumer<ExecuteContext> context
    ) throws CommonException {
        return this.executor.execute(
            key,
            (Input<I>) () -> this.serializer.deserialize(key, stream),
            context
        );
    }

    @Override
    public <I, O> Task<O> executeTask(
        final Key<I, O> key,
        final InputStream stream,
        final Consumer<ExecuteContext> context
    ) {
        final TaskBuilder<O> builder = this.factory.createObject(new TypeLiteral<>() { });
        return builder.add(
            () -> this.execute(
                key,
                stream,
                context
            )
        ).build();
    }

}
