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

package insideworld.engine.actions.chain.pre;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.chain.TestChainTags;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test pre-execute.
 * @since 0.14.0
 */
@QuarkusTest
class TestPreExecute {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    TestPreExecute(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Execute action with pre-execute with false condition if contains TWO tag.
     * ER: Output should be empty because child action shouldn't execute.
     * @throws CommonException Exception.
     */
    @Test
    final void testPreExecuteFalse() throws CommonException {
        this.preExecuteFalse(ParentAction.class);
    }

    /**
     * TC:
     * Execute action with pre-execute with true result and add to context TWO tag.
     * ER: Output should contain ONE and TWO tag.
     * @throws CommonException Exception.
     */
    @Test
    final void testPreExecuteTrue() throws CommonException {
        this.preExecuteTrue(ParentAction.class);
    }

    /**
     * The same testPreExecuteFalse but using DI.
     * @throws CommonException Exception.
     */
    @Test
    final void testPreExecuteDiFalse() throws CommonException {
        this.preExecuteFalse(ParentDiAction.class);
    }

    /**
     * The same testPreExecuteTrue but using DI.
     * @throws CommonException Exception.
     */
    @Test
    final void testPreExecuteDiTrue() throws CommonException {
        this.preExecuteTrue(ParentDiAction.class);
    }

    /**
     * Common method for TC of testPreExecuteFalse and testPreExecuteDIFalse.
     * @param action Action to execute.
     * @throws CommonException Exception.
     */
    private void preExecuteFalse(final Class<? extends Action> action) throws CommonException {
        final Context context = this.executor.createContext();
        context.put(TestChainTags.ONE, new Object());
        context.put(TestChainTags.TWO, new Object());
        final Output output = this.executor.execute(action, context);
        assert output.getRecords().size() == 0;
    }

    /**
     * Common method for TC of testPreExecuteTrue and testPreExecuteDITrue.
     * @param action Action to execute.
     * @throws CommonException Exception.
     */
    private void preExecuteTrue(final Class<? extends Action> action) throws CommonException {
        final Context context = this.executor.createContext();
        context.put(TestChainTags.ONE, new Object());
        final Output output = this.executor.execute(action, context);
        assert output.getRecords().size() == 1;
        final Record record = output.getRecords().iterator().next();
        assert record.contains(TestChainTags.TWO);
    }

}
