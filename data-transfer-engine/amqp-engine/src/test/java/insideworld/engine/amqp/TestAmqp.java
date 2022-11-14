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

import com.google.common.collect.ImmutableMap;
import com.rabbitmq.client.ConnectionFactory;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.Receiver;
import insideworld.engine.amqp.connection.Sender;
import insideworld.engine.properties.Properties;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.core.Vertx;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;
import io.vertx.mutiny.amqp.AmqpReceiver;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonSender;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
//import org.apache.qpid.server.Broker;
//import org.apache.qpid.server.BrokerOptions;
import org.apache.qpid.proton.amqp.messaging.AmqpValue;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.configuration.IllegalConfigurationException;
import org.apache.qpid.server.model.Broker;
import org.apache.qpid.server.model.SystemConfig;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestAmqp {

//    @Inject
//    Vertx vertx;

    @Inject
    Connection connection;

//    private final Properties properties;
//    private final AmqpSender sender;
//
//        @Inject
//    public TestAmqp(Properties properties, final AmqpSender sender) {
//        this.properties = properties;
//        this.sender = sender;
//    }


    @Test
    final void test() throws Exception {
        this.connection.createReceiver("test",
            (message) -> System.out.println(message.getArray())
        );
        final Sender test = this.connection.createSender("test");
        Map<String,Object>[] one = new Map[]{
            Map.of("one", 1, "two", "1"),
            Map.of("one", 2, "two", "2"),
            Map.of("one", 3, "two", "3"),
            Map.of("one", 3L, "two", "3"),
            Map.of("one", 111111111111L, "two", "3")
        };
        test.send(() -> one);

//
//        final AmqpConnection amqpConnection = AmqpClient.create(
//
//        ).connectAndAwait();
//
//        final AmqpReceiver qwerty = amqpConnection.createReceiverAndAwait("test");

        System.out.println("qwe");
    }

}
