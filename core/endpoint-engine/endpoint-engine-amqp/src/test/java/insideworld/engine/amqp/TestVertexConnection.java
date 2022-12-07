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

import insideworld.engine.amqp.vertex.VertexConnection;
import insideworld.engine.injection.ObjectFactory;
import io.vertx.amqp.AmqpClientOptions;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Vertx connections for tests.
 * @since 1.0.0
 */
@Singleton
public class TestVertexConnection extends VertexConnection {

    /**
     * Connect to test AQMP server.
     * @param factory Object factory.
     */
    @Inject
    public TestVertexConnection(final ObjectFactory factory) {
        super(new AmqpClientOptions()
            .setHost("localhost")
            .setPort(12345)
            .setUsername("qqq")
            .setPassword("qqq"),
            factory
        );
    }
}