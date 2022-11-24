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
import insideworld.engine.datatransfer.endpoint.actions.receiver.ActionReceiver;
import insideworld.engine.datatransfer.endpoint.actions.ActionSender;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.threads.Task;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.collections4.CollectionUtils;

public class AmqpActionReceiver implements OnStartUp, AmqpReceiver {

    private final static Consumer<Output> EMPTY_CONSUMER = output -> {
    };

    private final Connection connection;
    private final String channel;
    private final ActionReceiver<Map<String, Object>> receiver;
    private final ActionSender sender;
    private final ObjectFactory factory;

    public AmqpActionReceiver(
        final Connection connection,
        final String channel,
        final ActionReceiver<Map<String, Object>> receiver,
        final ActionSender sender,
        final ObjectFactory factory
    ) {
        this.connection = connection;
        this.channel = channel;
        this.receiver = receiver;
        this.sender = sender;
        this.factory = factory;
    }

    @Override
    public void receive(final Message message) {
        final Collection<Map<String, Object>> inputs = message.getArray();
        final Map<String, Object> properties = message.getProperties();
        inputs.forEach(input -> input.put(AmqpTags.AMQP_PROPERTIES.getTag(), properties));
        final Task<Output> task = this.receiver.execute(
            message.getSubject(),
            AmqpReceiveProfile.class,
            inputs
        );
        final Consumer<Output> consumer;
        if (properties.containsKey(AmqpTags.CALLBACK_ACTION.getTag()) && this.sender != null) {
            consumer = output -> AmqpActionReceiver.callback(properties, output);
        } else {
            consumer = AmqpActionReceiver.EMPTY_CONSUMER;
        }
        task.subscribe(consumer);
    }

    @Override
    public void startUp() {
        this.connection.createReceiver(this.channel, this);
    }

    @Override
    public int order() {
        return 70_000;
    }

    private void callback(final Map<String, Object> properties, final Output output) {
        if (CollectionUtils.isEmpty(output.getRecords())) {
            return;
        }
        final String action = (String) properties.get(AmqpTags.CALLBACK_ACTION.getTag());
        if (properties.containsKey(AmqpTags.BULK.getTag())) {
            final Map<String, Output> result =
                Collections.singletonMap(AmqpTags.BULK_OUTPUT.getTag(), output);
            this.sender.send(
          ,

            );
        } else {

        }

    }

}
