/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.receiver;

import insideworld.engine.amqp.destination.Channel;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpReceiver;

/**
 * Abstract action receiver. Using for receive messages from queue and process it.
 * Need to extend from this class and set in constructor necessary connection.
 * @since 0.4.0
 */
public abstract class AbstractReceiver implements Receiver {

    /**
     * Protected constructor.
     * @param connection Connection.
     * @param channel Destination of AMQP.
     */
    protected AbstractReceiver(final Channel channel) {
        final AmqpReceiver receiver = channel.createReceiver();
        receiver.handler(this::process);
    }

    /**
     * Abstract method for define a behavior which need to make with received message.
     * @param message Message.
     */
    protected abstract void process(AmqpMessage message);
}
