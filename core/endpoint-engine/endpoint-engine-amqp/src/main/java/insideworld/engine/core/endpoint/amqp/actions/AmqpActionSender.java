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

import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.endpoint.amqp.actions.tags.AmqpTags;
import insideworld.engine.core.endpoint.amqp.connection.AmqpSender;
import insideworld.engine.core.endpoint.amqp.connection.Connection;
import insideworld.engine.core.endpoint.amqp.connection.Message;
import insideworld.engine.core.endpoint.base.action.ActionSender;
import insideworld.engine.core.endpoint.base.action.PreSend;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.startup.OnStartUp;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * AMQP action sender. Using to send message to channel.
 * @since 0.14.0
 */
public class AmqpActionSender implements ActionSender, OnStartUp {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Connection.
     */
    private final Connection connection;

    /**
     * Channel to send.
     */
    private final String channel;

    /**
     * Pre sends.
     */
    private final Collection<PreSend<Message>> pres;

    /**
     * AMQP sender.
     */
    private AmqpSender sender;

    /**
     * Default constructor.
     * @param connection Connection instance.
     * @param channel Channel name.
     * @param pres Pre send objects.
     * @param factory Object factory.
     */
    public AmqpActionSender(
        final ObjectFactory factory,
        final Connection connection,
        final String channel,
        final Collection<PreSend<Message>> pres
    ) {
        this.factory = factory;
        this.connection = connection;
        this.channel = channel;
        this.pres = pres;
    }

    @Override
    public final void send(final String action, final String callback, final Output output) {
        final Map<String, Object> properties;
        if (callback == null) {
            properties = Collections.emptyMap();
        } else {
            properties = Map.of(AmqpTags.CALLBACK_ACTION.getTag(), callback);
        }
        final Message message =
            this.factory.createObject(OutputMessage.class, output, action, properties);
        for (final PreSend<Message> pre : this.pres) {
            pre.execute(message);
        }
        this.sender.send(message);
    }

    @Override
    public final void startUp() {
        this.sender = this.connection.createSender(this.channel);
    }

    @Override
    public final long startOrder() {
        return 900_000;
    }
}
