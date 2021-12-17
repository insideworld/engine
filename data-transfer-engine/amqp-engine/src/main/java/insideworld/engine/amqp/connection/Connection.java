/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.connection;

import io.vertx.mutiny.amqp.AmqpConnection;

/**
 * Inteface is using for work with Vertx connection.
 */
public interface Connection {

    /**
     * Get connection.
     * @return AMQP connection.
     */
    AmqpConnection connection();
}
