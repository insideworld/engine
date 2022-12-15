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

import com.google.common.collect.Lists;
import insideworld.engine.core.endpoint.amqp.connection.AmqpSender;
import insideworld.engine.core.endpoint.amqp.connection.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;

public class VertexAmqpSender implements AmqpSender {

    private final io.vertx.mutiny.amqp.AmqpSender amqpSender;

    public VertexAmqpSender(final AmqpConnection connection, final String channel) {
        this.amqpSender = connection.createSenderAndAwait(channel);
    }

    @Override
    public void send(final Message message) {
        final AmqpMessageBuilder builder = AmqpMessage.create();
        builder.withJsonArrayAsBody(new JsonArray(Lists.newArrayList(message.getArray())));
        builder.subject(message.getSubject());
        builder.applicationProperties(new JsonObject(message.getProperties()));
        this.amqpSender.send(builder.build());
    }
}
