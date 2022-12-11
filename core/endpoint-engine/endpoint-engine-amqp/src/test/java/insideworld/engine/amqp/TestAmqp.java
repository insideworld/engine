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

import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.test.quarkus.amqp.QpidTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.hamcrest.MatcherAssert;
import org.testcontainers.shaded.org.hamcrest.Matchers;

/**
 * Test for AMQP.
 * @since 0.14.0
 */
@QuarkusTest
@QuarkusTestResource(QpidTestResource.class)
public class TestAmqp {

    /**
     * Test sender instance.
     */
    private final TestSender sender;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Callback action.
     */
    private TestCallbackAction callback;

    /**
     * Constructor.
     * @param sender Test sender.
     * @param factory Object factory.
     * @param callback Callback action.
     */
    @Inject
    public TestAmqp(
        final TestSender sender,
        final ObjectFactory factory,
        final TestCallbackAction callback
    ) {
        this.sender = sender;
        this.factory = factory;
        this.callback = callback;
    }

    /**
     * TC: Execute action which return 3 record using amqp.
     * ER: After send an action expected that callback action receive 3 values in separate threads.
     * Because operation is async just wait some time and after that check received values.
     */
    @Test
    final void testSendReceive() throws InterruptedException {
        final Output output = this.factory.createObject(Output.class);
        output.createRecord().put("testValue", "My value!");
        this.sender.send(
            "insideworld.engine.amqp.TestAction",
            "insideworld.engine.amqp.TestCallbackAction",
            output
        );
        Thread.sleep(100);
        MatcherAssert.assertThat(
            "Callback action doesn't contains necessary values",
            this.callback.values(),
            Matchers.allOf(
                Matchers.iterableWithSize(3),
                Matchers.hasItems(
                    "One more",
                    "Second",
                    "And one more else"
                )
            )
        );
    }

}
