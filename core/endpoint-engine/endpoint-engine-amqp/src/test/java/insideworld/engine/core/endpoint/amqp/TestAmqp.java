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

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.action.executor.key.StringKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.amqp.one.CreateTestDataAction;
import insideworld.engine.core.endpoint.amqp.one.ReceiveTestDataAction;
import insideworld.engine.frameworks.quarkus.test.amqp.QpidTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
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

    private final ActionExecutor executor;

    @Inject
    public TestAmqp(final ActionExecutor executor) {
        this.executor = executor;
    }

    /**
     * TC: Execute action which return 3 record using amqp.
     * ER: After send an action expected that callback action receive 3 values in separate threads.
     * Because operation is async just wait some time and after that check received values.
     */
    @Test
    final void testSendReceive() throws CommonException, InterruptedException {
        final UUID uuid = UUID.randomUUID();
        this.executor.execute(
            new ClassKey<>(CreateTestDataAction.class),
            uuid
        );
        Thread.sleep(100);
        MatcherAssert.assertThat(
            "Callback action doesn't execute",
            ReceiveTestDataAction.DATA,
            Matchers.hasProperty("value", Matchers.is(uuid))
        );
    }

}
