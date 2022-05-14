/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.sender;

import insideworld.engine.amqp.destination.Channel;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;
import io.vertx.mutiny.amqp.AmqpSender;

/**
 * Abstract class using for set up send AMQP messages by specific connection.
 *
 * @since 0.4.0
 * @param <T> Type of payload.
 */
public abstract class AbstractSender<T> implements Sender<T> {

    /**
     * Vertx sender.
     */
    private final AmqpSender sender;

    /**
     * Default constructor.

     */
    public AbstractSender(final Channel channel) {
        this.sender = channel.createSender();
    }

    @Override
    public final void send(T data) throws Exception {
        final AmqpMessageBuilder builder = AmqpMessage.create();
        this.build(data, builder);
        final AmqpSender send = this.sender.send(builder.build());
        send.endAndForget();
    }

    /**
     * Build AMQP message.
     * @param data Payload.
     * @param builder Message builder.
     */
    protected abstract void build(T data, AmqpMessageBuilder builder) throws Exception;

}
