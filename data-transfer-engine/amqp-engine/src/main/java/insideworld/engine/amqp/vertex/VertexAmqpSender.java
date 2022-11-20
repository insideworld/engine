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

import insideworld.engine.amqp.connection.AmqpSender;
import insideworld.engine.amqp.connection.message.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpMessage;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;
import java.util.List;
import javax.enterprise.context.Dependent;

@Dependent
public class VertexAmqpSender implements AmqpSender {

    private io.vertx.mutiny.amqp.AmqpSender amqpSender;

    public VertexAmqpSender init(final String channel, final AmqpConnection connection) {
        this.amqpSender = connection.createSenderAndAwait(channel);
        return this;
    }

    @Override
    public void send(final Message message) {
        final AmqpMessageBuilder builder = AmqpMessage.create();
        builder.withJsonArrayAsBody(new JsonArray(List.of(message.getArray())));
        builder.subject(message.getSubject());
        builder.applicationProperties(new JsonObject(message.getProperties()));
        this.amqpSender.send(builder.build());
    }
}
