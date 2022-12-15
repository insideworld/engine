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

package insideworld.engine.core.endpoint.amqp.actions;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.endpoint.amqp.actions.tags.AmqpTags;
import insideworld.engine.core.endpoint.amqp.connection.AmqpReceiver;
import insideworld.engine.core.endpoint.amqp.connection.Connection;
import insideworld.engine.core.endpoint.amqp.connection.Message;
import insideworld.engine.core.endpoint.base.action.AbstractActionReceiver;
import insideworld.engine.core.endpoint.base.action.ActionSender;
import insideworld.engine.core.endpoint.base.action.ContextBuilder;
import insideworld.engine.core.endpoint.base.action.OutputTaskBuilder;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.common.threads.Task;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of action receiver to integrate AMQP into action engine.
 * @since 1.0.0
 */
public class AmqpActionReceiver extends AbstractActionReceiver<Message> implements OnStartUp {

    /**
     * Connections.
     */
    private final Connection connection;

    /**
     * Receive channel.
     */
    private final String channel;

    /**
     * Action sender for callbacks.
     */
    private final ActionSender sender;

    /**
     * Default constructor.
     * @param builder Task builder for output.
     * @param executor Action executor.
     * @param connection AMQP connection.
     * @param channel Channel name to receive.
     * @param sender Action sender for callback (can be null if you want to disable callbacks).
     */
    public AmqpActionReceiver(
        final OutputTaskBuilder builder,
        final ActionExecutor<String> executor,
        final Connection connection,
        final String channel,
        final ActionSender sender
    ) {
        super(builder, executor, AmqpReceiveProfile.class);
        this.connection = connection;
        this.channel = channel;
        this.sender = sender;
    }

    @Override
    public final void startUp() {
        final AmqpReceiver receiver = this.connection.createReceiver(this.channel);
        receiver.receive(message -> {
            final Task<Output> task = this.execute(message.getSubject(), message);
            task.subscribe(output -> this.callback(output, message));
        });
    }

    @Override
    public final long startOrder() {
        return 900_000;
    }

    @Override
    protected final Collection<ContextBuilder> contextBuilders(final Message parameter) {
        return parameter
            .getArray()
            .stream()
            .map(map -> this.predicate(map, parameter))
            .collect(Collectors.toList());
    }

    /**
     * Method to send callback messages.
     * If sender is not set - nothing to do.
     * @param output Outputs.
     * @param message Message.
     */
    private void callback(final Output output, final Message message) {
        if (this.sender == null) {
            return;
        }
        final String action = (String) message
            .getProperties()
            .get(AmqpTags.CALLBACK_ACTION.getTag());
        if (StringUtils.isEmpty(action) || CollectionUtils.isEmpty(output.getRecords())) {
            return;
        }
        this.sender.send(action, null, output);
    }

    /**
     * Create a predicate with converted from map contexts and added AMQP related properties.
     * @param map Map from payload.
     * @param parameter All message.
     * @return Context builder.
     */
    private ContextBuilder predicate(final Map<String, Object> map, final Message parameter) {
        return () -> {
            final Context context = this.createContext();
            context.put(AmqpTags.MESSAGE, parameter);
            map.forEach(context::put);
            return context;
        };
    }

}
