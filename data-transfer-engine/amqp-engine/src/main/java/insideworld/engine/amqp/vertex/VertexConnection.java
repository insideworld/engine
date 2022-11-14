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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import insideworld.engine.amqp.connection.Connection;
import insideworld.engine.amqp.connection.Receiver;
import insideworld.engine.amqp.connection.Sender;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import io.vertx.amqp.AmqpClientOptions;
import io.vertx.mutiny.amqp.AmqpClient;
import io.vertx.mutiny.amqp.AmqpConnection;
import java.util.Map;
import javax.inject.Singleton;

public class VertexConnection implements Connection, OnStartUp {

    private final AmqpClientOptions options;
    private final ObjectFactory factory;
    private AmqpConnection connection;

    public VertexConnection(final AmqpClientOptions options,
                            final ObjectFactory factory) {
        this.options = options;
        this.factory = factory;
    }

    @Override
    public void createReceiver(final String channel, final Receiver receiver) {
        this.factory
            .createObject(VertexReceiver.class, this.connection)
            .init(channel, receiver);
    }

    @Override
    public Sender createSender(final String channel) {
        return this.factory
            .createObject(VertexSender.class)
            .init(channel, this.connection);
    }

    @Override
    public void startUp() throws StartUpException {
        this.connection = AmqpClient.create(this.options).connectAndAwait();
    }

    @Override
    public int order() {
        return 50_000;
    }
}
