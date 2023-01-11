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

package insideworld.engine.core.action.executors;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.action.executor.key.StringKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test class and key executors of action.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestKeyExecutor {

    /**
     * Action executor.
     */
    private final ActionExecutor executor;


    /**
     * Default constructor.
     *
     * @param cexecutor Class action executor.
     * @param kexecutor Key action executor.
     */
    @Inject
    TestKeyExecutor(final ActionExecutor executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Execute the test action using class action executor.
     * ER:
     * Output should contain one record with UUID tag from context.
     *
     * @throws CommonException Exception.
     */
    @Test
    final void testClass() throws CommonException {
        this.test(new ClassKey<>(TestAction.class));
    }

    /**
     * TC:
     * Execute the test action using string action executor.
     * ER:
     * Output should contain one record with UUID tag from context.
     *
     * @throws CommonException Exception.
     */
    @Test
    final void testKey() throws CommonException {
        this.test(
            new StringKey<>("insideworld.engine.tests.unit.actions.executors.key.TestAction")
        );
    }

    /**
     * TC: Execute different ActionExceptions.
     * Exception during action processing.
     * ER: For each case right message.
     */
    @Test
    final void testExceptions() {
        MatcherAssert.assertThat(
            "Wrong exception",
            () -> this.executor.execute(new ClassKey<>(ExceptionAction.class), 1),
            ExceptionMatchers.catchException(
                ActionException.class,
                ExceptionMatchers.messageMatcher(
                    0, Matchers.containsString("Exception!")
                )
            )
        );
        MatcherAssert.assertThat(
            "Wrong exception",
            () -> this.executor.execute(new ClassKey<>(ExceptionAction.class), 2),
            ExceptionMatchers.catchException(
                ActionException.class,
                ExceptionMatchers.messageMatcher(
                    1, Matchers.containsString("Unhandled")
                )
            )
        );
    }

    /**
     * Common method to execute action with different executors.
     *
     * @param executor Executor.
     * @param key Action key.
     * @param <T> Type of action key.
     * @throws ActionException Exception.
     */
    private void test(final Key<UUID, UUID> key) throws CommonException {
        final UUID uuid = UUID.randomUUID();
        final UUID output = this.executor.execute(key, uuid);
        MatcherAssert.assertThat(
            "Output contains record with tag UUID",
            output,
            Matchers.is(uuid)
        );
    }

}
