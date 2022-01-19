/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
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

    public static void some() {

    }
}
