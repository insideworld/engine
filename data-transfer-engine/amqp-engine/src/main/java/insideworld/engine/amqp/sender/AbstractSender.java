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
    private Channel channel;

    /**
     * Default constructor.

     */
    public AbstractSender(final Channel channel) {
        this.sender = channel.createSender();
        this.channel = channel;
    }

    @Override
    public final void send(T data) throws Exception {
        final AmqpMessageBuilder builder = AmqpMessage.create();
        this.build(data, builder);
        this.sender.writeAndForget(builder.build());
    }

    /**
     * Build AMQP message.
     * @param data Payload.
     * @param builder Message builder.
     */
    protected abstract void build(T data, AmqpMessageBuilder builder) throws Exception;

}
