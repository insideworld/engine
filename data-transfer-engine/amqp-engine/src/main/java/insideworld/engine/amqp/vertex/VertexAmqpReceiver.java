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

import insideworld.engine.amqp.connection.AmqpReceiver;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.message.Message;
import insideworld.engine.injection.ObjectFactory;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpMessage;


public class VertexAmqpReceiver {
    private final ObjectFactory factory;

    final io.vertx.mutiny.amqp.AmqpReceiver receiver;

    public VertexAmqpReceiver(
        final ObjectFactory factory,
        final AmqpConnection connection,
        final String channel
    ) {
        this.factory = factory;
        this.receiver = connection.createReceiverAndAwait(channel)
            .handler(message -> amqpReceiver.receive(this.convert(message)));
    }

    public VertexAmqpReceiver init(final String channel, final AmqpReceiver amqpReceiver) {
        final io.vertx.mutiny.amqp.AmqpReceiver receiver =
        return this;
    }

    private Message convert(final AmqpMessage message) {
        return this.factory.createObject(VertexMessage.class, message);
    }

}
