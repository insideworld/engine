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

package insideworld.engine.amqp;

import insideworld.engine.amqp.connection.AmqpSender;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.message.Message;
import insideworld.engine.amqp.connection.message.SendMessage;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.annotations.Blocking;
import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestAmqp {

    private final Connection connection;
    private final ObjectFactory factory;

    @Inject
    public TestAmqp(
        final Connection connection,
        final ObjectFactory factory
    ) {
        this.connection = connection;
        this.factory = factory;
    }

    @Test
    final void test() throws Exception {
        this.connection.createReceiver("test",
            this::receive
        );
        final AmqpSender test = this.connection.createSender("test");
        Map<String,Object>[] one = new Map[]{
            Map.of("one", 1, "two", "1"),
            Map.of("one", 2, "two", "2"),
            Map.of("one", 3, "two", "3"),
            Map.of("one", 3L, "two", "3"),
            Map.of("one", 111111111111L, "two", "3")
        };
        final SendMessage message =
            this.factory.createObject(SendMessage.class, one, Collections.emptyMap(), "qwe");
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        test.send(message);
        System.out.println("qwe");
    }

    private void receive(Message message) {
        System.out.println(message.getArray());
    }

}
