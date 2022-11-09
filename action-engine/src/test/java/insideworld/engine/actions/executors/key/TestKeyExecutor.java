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

package insideworld.engine.actions.executors.key;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executors.TestExecutorTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
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
        boolean exception = false;
        try {
            this.cexecutor.execute(ExceptionAction.class, context.cloneContext());
        } catch (final ActionException exp) {
            exception = exp.getMessage().contains("Exception!");
        }
        assert exception;
        context.put(TestExecutorTags.EXCEPTION, 2, true);
        try {
            this.cexecutor.execute(ExceptionAction.class, context.cloneContext());
        } catch (final ActionException exp) {
            exception = exp.getCause().getMessage().contains("Unhandled");
        }
        assert exception;
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
        assert output.getRecords().size() == 1;
        assert uuid.equals(output.iterator().next().get(TestExecutorTags.UUID));
    }

}
