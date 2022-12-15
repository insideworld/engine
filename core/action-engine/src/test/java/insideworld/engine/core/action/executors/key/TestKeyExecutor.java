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

package insideworld.engine.core.action.executors.key;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executors.TestExecutorTags;
import insideworld.engine.core.action.keeper.Record;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class and key executors of action.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestKeyExecutor {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> cexecutor;

    /**
     * Key action executor.
     */
    private final ActionExecutor<String> kexecutor;

    /**
     * Default constructor.
     *
     * @param cexecutor Class action executor.
     * @param kexecutor Key action executor.
     */
    @Inject
    TestKeyExecutor(
        final ActionExecutor<Class<? extends Action>> cexecutor,
        final ActionExecutor<String> kexecutor) {
        this.cexecutor = cexecutor;
        this.kexecutor = kexecutor;
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
        TestKeyExecutor.test(this.cexecutor, TestAction.class);
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
        TestKeyExecutor.test(
            this.kexecutor,
            "insideworld.engine.tests.unit.actions.executors.key.TestAction"
        );
    }

    /**
     * TC: Execute different ActionExceptions.
     * Exception during action processing.
     * ER: For each case right message.
     */
    @Test
    final void testExceptions() {
        final Context context = this.cexecutor.createContext();
        context.put(TestExecutorTags.EXCEPTION, 1, true);
        final ActionException handle = Assertions.assertThrows(
            ActionException.class,
            () -> this.cexecutor.execute(ExceptionAction.class, context.cloneContext()),
            "Action exception is expected"
        );
        MatcherAssert.assertThat(
            "It's not a handle exception",
            handle,
            ExceptionMatchers.messageMatcher(
                0, Matchers.containsString("Exception!")
            )
        );
        context.put(TestExecutorTags.EXCEPTION, 2, true);
        final ActionException unhandle = Assertions.assertThrows(
            ActionException.class,
            () -> this.cexecutor.execute(ExceptionAction.class, context.cloneContext()),
            "Action exception is expected"
        );
        MatcherAssert.assertThat(
            "It's not a handle exception",
            unhandle,
            ExceptionMatchers.messageMatcher(
                1, Matchers.containsString("Unhandled")
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
    private static <T> void test(final ActionExecutor<T> executor, final T key)
        throws ActionException {
        final Context context = executor.createContext();
        final UUID uuid = UUID.randomUUID();
        context.put(TestExecutorTags.UUID, uuid);
        final Output output = executor.execute(key, context);
        MatcherAssert.assertThat(
            "Output contains record with tag UUID",
            output,
            Matchers
                .both(Matchers.<Record>iterableWithSize(1))
                .and(
                    Matchers.hasItem(
                        KeeperMatchers.match(
                            TestExecutorTags.UUID, Matchers.is(uuid)
                        )
                    )
                )
        );
    }

}
