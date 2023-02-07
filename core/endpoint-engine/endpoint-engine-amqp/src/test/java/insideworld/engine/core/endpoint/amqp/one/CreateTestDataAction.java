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

package insideworld.engine.core.endpoint.amqp.one;

import insideworld.engine.core.endpoint.base.action.serializer.ActionSerializer;
import insideworld.engine.core.endpoint.amqp.actions.AmqpActionSender;
import insideworld.engine.core.endpoint.amqp.connection.TestVertexConnection;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateTestDataAction extends AmqpActionSender<UUID> {
    /**
     * Default constructor.
     *
     * @param connection
     * @param serializer
     */
    @Inject
    public CreateTestDataAction(final TestVertexConnection connection, final ActionSerializer serializer) {
        super(connection, serializer);
    }

    @Override
    public String key() {
        return "[one]CreateTestDataAction";
    }

    @Override
    public void types(final UUID input, final Void output) {
        //Nothing to do.
    }

    @Override
    protected String remoteKey() {
        return "[two]CreateTestDataAction";
    }

    @Override
    protected String callbackKey() {
        return "[two]SendTestDataAction";
    }

    @Override
    protected String channel() {
        return "test";
    }
}
