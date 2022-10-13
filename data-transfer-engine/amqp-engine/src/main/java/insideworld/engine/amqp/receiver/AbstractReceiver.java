/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
