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

package insideworld.engine.amqp.actions;

import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.connection.AmqpSender;
import insideworld.engine.threads.ThreadPool;
import java.util.Map;
import java.util.concurrent.Future;

public class AmqpExecuteFacade {

    private final ThreadPool pool;
    private final ActionExecutor<String> executor;
    private final AmqpSender amqpSender;

    public AmqpExecuteFacade(final ThreadPool pool,
                             final ActionExecutor<String> executor,
                             final AmqpSender amqpSender) {

        this.pool = pool;
        this.executor = executor;
        this.amqpSender = amqpSender;
    }

    public Future<Output> execute(final String action, final Map<String, Object> map) {
        return this.pool.execute(() -> {
            final Context context = this.executor.createContext();
            map.forEach(context::put);
            return this.executor.execute(action, context);
        });
    }

    public void send(final String action, final Output output) {

    }

}
