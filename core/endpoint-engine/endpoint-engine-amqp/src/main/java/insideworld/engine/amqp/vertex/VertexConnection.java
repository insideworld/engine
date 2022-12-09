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

package insideworld.engine.amqp.vertex;

import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.AmqpReceiver;
import insideworld.engine.amqp.connection.AmqpSender;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;

/**
 * Connection implementation based on Vertx.
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

    /**
     * Default constructor.
     * @param options Client options.
     * @param factory Object factory.
     */
    public VertexConnection(final AmqpClientOptions options,
                            final ObjectFactory factory) {
        this.options = options;
        this.factory = factory;
    }

    @Override
    public final AmqpReceiver createReceiver(final String channel) {
        return this.factory.createObject(VertexAmqpReceiver.class, this.connection, channel);
    }

    @Override
    public final AmqpSender createSender(final String channel) {
        return this.factory.createObject(VertexAmqpSender.class, this.connection, channel);
    }

    @Override
    public final void startUp() {
        this.connection = AmqpClient.create(this.options).connectAndAwait();
    }

    @Override
    public final int startOrder() {
        return 50_000;
    }
}
