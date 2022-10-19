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

package insideworld.engine.amqp.connection;

import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;

/**
 * Abstract class for create an AMQP connections.
 * @since 0.4.0
 */
public abstract class AbstractConnection implements Connection {

    /**
     * Vertx connection.
     */
    private final AmqpConnection connection;

    /**
     * Default constructor.
     * @param host Host of AMQP service.
     * @param port Port of AMQP service.
     * @param user User.
     * @param password Password.
     */
    public AbstractConnection(final String host,
                              final int port,
                              final String user,
                              final String password) {
        final AmqpClientOptions options = new AmqpClientOptions()
            .setHost(host)
            .setPort(port)
            .setUsername(user)
            .setPassword(password);
        final AmqpClient client = AmqpClient.create(options);
        this.connection = client.connectAndAwait();
    }

    @Override
    public final AmqpConnection connection() {
        return this.connection;
    }

}
