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

package insideworld.engine.core.endpoint.amqp.vertex;

import insideworld.engine.core.endpoint.amqp.connection.AmqpReceiver;
import insideworld.engine.core.endpoint.amqp.connection.AmqpSender;
import insideworld.engine.core.endpoint.amqp.connection.Connection;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.startup.OnStartUp;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Connection implementation based on Vertx.
 * This class init after start up.
 * @since 0.14.0
 */
public class VertexConnection implements Connection, OnStartUp {

    /**
     * Amqp client options.
     */
    private final AmqpClientOptions options;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Vertx connection.
     */
    private AmqpConnection connection;

    private final Map<String, AmqpReceiver> receivers;

    private final Map<String, AmqpSender> senders;

    /**
     * Default constructor.
     * @param options Client options.
     * @param factory Object factory.
     */
    public VertexConnection(final AmqpClientOptions options,
                            final ObjectFactory factory) {
        this.options = options;
        this.factory = factory;
        this.receivers = new ConcurrentHashMap<>();
        this.senders = new ConcurrentHashMap<>();
    }

    @Override
    public final AmqpReceiver createReceiver(final String channel) {
        AmqpReceiver receiver = this.receivers.get(channel);
        if (receiver == null) {
            synchronized (this) {
                receiver = this.receivers.get(channel);
                if (receiver == null) {
                    receiver = this.factory.createObject(
                        VertexAmqpReceiver.class,
                        this.connection,
                        channel
                    );
                    this.receivers.put(channel, receiver);
                }
            }
        }
        return receiver;
    }

    @Override
    public final AmqpSender createSender(final String channel) {
        AmqpSender sender = this.senders.get(channel);
        if (sender == null) {
            synchronized (this) {
                sender = this.senders.get(channel);
                if (sender == null) {
                    sender =  this.factory.createObject(
                        VertexAmqpSender.class,
                        this.connection,
                        channel
                    );
                    this.senders.put(channel, sender);
                }
            }
        }
        return sender;
    }

    @Override
    public final void startUp() {
        this.connection = AmqpClient.create(this.options).connectAndAwait();
    }

    @Override
    public final long startOrder() {
        return 1_000_000;
    }
}
