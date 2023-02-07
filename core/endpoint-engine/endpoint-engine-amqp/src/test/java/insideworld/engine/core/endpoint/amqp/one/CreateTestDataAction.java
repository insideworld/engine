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

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.executor.key.StringKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.amqp.connection.TestSender;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateTestDataAction implements Action<UUID, Void> {

    private final TestSender sender;

    @Inject
    public CreateTestDataAction(final TestSender sender) {
        this.sender = sender;
    }

    @Override
    public Void execute(final UUID input) throws CommonException {
        this.sender.execute(
            new StringKey<>("insideworld.engine.core.endpoint.amqp.two.CreateTestDataAction"),
            new StringKey<>("insideworld.engine.core.endpoint.amqp.one.ReceiveTestDataAction"),
            input
        );
        return null;
    }

    @Override
    public String key() {
        return "insideworld.engine.core.endpoint.amqp.one.CreateTestDataAction";
    }

    @Override
    public void types(final UUID input, final Void output) {

    }
}