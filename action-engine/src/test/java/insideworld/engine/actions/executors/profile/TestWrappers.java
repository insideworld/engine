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

package insideworld.engine.actions.executors.profile;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executors.TestExecutorTags;
import insideworld.engine.actions.keeper.context.Context;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.Iterator;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test wrappers and they execution sequence.
 * @since 0.14.0
 */
@QuarkusTest
class TestWrappers {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     */
    @Inject
    TestWrappers(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Create LinkedList for check order of executed wrappers.
     * Create 4 wrappers with order from 3 to -1 where 0 is the execute action wrapper.
     * After that execute dummy action.
     * ER:
     * Sequence should contain values from 3 to -1 with right order.
     */
    @Test
    final void test() {
        final Context context = this.executor.createContext();
        context.put(TestExecutorTags.SEQUENCE, Lists.newLinkedList());
        this.executor.execute(DummyAction.class, context);
        final Collection<Integer> sequence = context.get(TestExecutorTags.SEQUENCE);
        final Iterator<Integer> iterator = sequence.iterator();
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 3; idx > -1; idx--) {
            assert iterator.hasNext();
            assert iterator.next().equals(idx);
        }
    }
}
