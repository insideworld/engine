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

package insideworld.engine.core.endpoint.amqp.two;

import insideworld.engine.core.endpoint.base.action.serializer.ActionSerializer;
import insideworld.engine.core.endpoint.amqp.TestData;
import insideworld.engine.core.endpoint.amqp.actions.AmqpActionSender;
import insideworld.engine.core.endpoint.amqp.connection.TestVertexConnection;
import javax.inject.Singleton;

@Singleton
public class SendTestDataAction extends AmqpActionSender<TestData> {
    /**
     * Default constructor.
     *
     * @param connection
     * @param serializer
     */
    public SendTestDataAction(final TestVertexConnection connection, final ActionSerializer serializer) {
        super(connection, serializer);
    }

    @Override
    public String key() {
        return "[two]SendTestDataAction";
    }

    @Override
    public void types(final TestData input, final Void output) {

    }

    @Override
    protected String remoteKey() {
        return "[one]ReceiveTestDataAction";
    }

    @Override
    protected String callbackKey() {
        return null;
    }

    @Override
    protected String channel() {
        return "test";
    }
}
