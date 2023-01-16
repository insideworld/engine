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

package insideworld.engine.core.endpoint.amqp;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import java.util.UUID;
import javax.inject.Singleton;

/**
 * Test action for AMQP.
 * Just create 3 records in output.
 * @since 0.14.0
 */
@Singleton
public class TestCallbackAction implements Action<TestData, Void> {

    public static UUID INCOME;

    @Override
    public Void execute(final TestData input) throws CommonException {
        TestCallbackAction.INCOME = input.getValue();
        return null;
    }

    @Override
    public final String key() {
        return "insideworld.engine.core.endpoint.amqp.TestCallbackAction";
    }

    @Override
    public Class<? extends TestData> inputType() {
        return TestData.class;
    }

    @Override
    public Class<? extends Void> outputType() {
        return Void.TYPE;
    }
}
