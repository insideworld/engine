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

import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.amqp.actions.tags.AmqpTags;
import insideworld.engine.amqp.connection.AmqpReceiver;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.message.Message;
import insideworld.engine.datatransfer.endpoint.actions.ActionSender;
import insideworld.engine.datatransfer.endpoint.actions.ActionEndpoint;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.threads.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class AmqpActionReceiver implements OnStartUp {

    private final Connection connection;
    private final String channel;
    private final ActionSender sender;
    private final ActionEndpoint<Message> facade;

    public AmqpActionReceiver(
        final Connection connection,
        final String channel,
        final ActionSender sender,
        final ActionEndpoint<Message> facade
    ) {
        this.connection = connection;
        this.channel = channel;
        this.sender = sender;
        this.facade = facade;
    }

    @Override
    public void startUp() {
        final AmqpReceiver receiver = this.connection.createReceiver(this.channel);
        receiver.receive(message -> {
            final Task<Output> task = this.facade.execute(message.getSubject(), message);
            task.subscribe(output -> this.callback(output, message));

        });

    }

    @Override
    public int order() {
        return 70_000;
    }

    private void callback(final Output output, final Message receive) {
        final String action = (String) receive
            .getProperties()
            .get(AmqpTags.CALLBACK_ACTION.getTag());
        if (StringUtils.isEmpty(action) || CollectionUtils.isEmpty(output.getRecords())) {
            return;
        }
        this.sender.send(action, null, output);
    }

}
