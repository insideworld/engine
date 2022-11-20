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

import com.google.common.collect.Lists;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.AmqpReceiver;
import insideworld.engine.amqp.connection.message.Message;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

public class AmqpActionReceiver implements OnStartUp {

    private final Connection connection;
    private final String channel;
    private final AmqpExecuteFacade executor;

    public AmqpActionReceiver(
        final Connection connection,
        final String channel,
        final ObjectFactory factory
        ) {
        this.connection = connection;
        this.channel = channel;
        this.executor = executor;
    }

    @Override
    public void receive(final Message message) {
        final Map<String, Object>[] inputs = message.getArray();
        final Collection<Future<Output>> result = Lists.newArrayListWithCapacity(inputs.length);
        for (final Map<String, Object> input : inputs) {
            result.add(this.executor.execute(message.getSubject(), input));
        }
        for (final Future<Output> future : result) {

        }
    }



    @Override
    public void startUp() {
        this.connection.createReceiver(this.channel, this);
    }


    @Override
    public int order() {
        return 70_000;
    }

}
