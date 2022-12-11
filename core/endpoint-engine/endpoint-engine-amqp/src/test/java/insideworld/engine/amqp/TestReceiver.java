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

import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.amqp.actions.AmqpActionReceiver;
import insideworld.engine.datatransfer.endpoint.actions.OutputTaskBuilder;
import javax.inject.Singleton;

/**
 * Test receiver.
 * @since 0.14.0
 */
@Singleton
public class TestReceiver extends AmqpActionReceiver {

    /**
     * Default constructor.
     *
     * @param builder Task builder for output.
     * @param executor Action executor.
     * @param connection AMQP connection.
     * @param sender Action sender for callback (can be null if you want to disable callbacks).
     */
    public TestReceiver(final OutputTaskBuilder builder,
                        final ActionExecutor<String> executor,
                        final TestVertexConnection connection,
                        final TestSender sender
    ) {
        super(builder, executor, connection, "test", sender);
    }
}
