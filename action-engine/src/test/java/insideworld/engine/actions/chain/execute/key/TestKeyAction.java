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

package insideworld.engine.actions.chain.execute.key;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.chain.execute.TestChainTags;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Execute action by specific key without additional arguments.
 * See test method comments.
 * @since 0.14.0
 */
@QuarkusTest
class TestKeyAction {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Results for compare output.
     */
    private final Map<String, Object> results;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    TestKeyAction(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
        this.results = Map.of(
            TestChainTags.UUID.getTag(), UUID.randomUUID(),
            TestChainTags.OUTPUT_ADDITIONAL.getTag(), "ChildAction"
        );
    }

    /**
     * TC: Execute action from chain action and add only action parameter using class key.
     * ER: In child action should add to output UUID from parent context and add one more record
     * to test merge.
     * @throws CommonException Exception.
     */
    @Test
    final void testClass() throws CommonException {
        this.executeAction(ParentClassAction.class);
    }

    /**
     * TC: Execute action from chain action and add only action parameter using string key.
     * ER: In child action should add to output UUID from parent context and add one more record
     * to test merge.
     * @throws CommonException Exception.
     */
    @Test
    final void testString() throws CommonException {
        this.executeAction(ParentStringAction.class);
    }

    /**
     * TC: Execute action where not set an action key in link.
     * ER: Should throw exception.
     */
    @Test
    final void testNull() {
        boolean exception = false;
        try {
            this.executeAction(ParentNullAction.class);
        } catch (final CommonException exp) {
            if (exp.getMessage().contains("Action is not set!")) {
                exception = true;
            }
        }
        assert exception;
    }

    /**
     * Just execute action and make compare by UUID and Additional tag.
     * @param action Test action to execute.
     * @throws CommonException Exception.
     */
    private void executeAction(final Class<? extends Action> action) throws CommonException {
        final Context context = this.executor.createContext();
        context.put(TestChainTags.UUID, (UUID) this.results.get(TestChainTags.UUID.getTag()));
        final Output output = this.executor.execute(action, context);
        for (final Record record : output) {
            final Map<String, ? super Object> values = record.values();
            assert values.size() == 1;
            final Map.Entry<String, ? super Object> next = values.entrySet().iterator().next();
            assert this.results.containsKey(next.getKey());
            assert this.results.get(next.getKey()).equals(next.getValue());
        }
    }
}
