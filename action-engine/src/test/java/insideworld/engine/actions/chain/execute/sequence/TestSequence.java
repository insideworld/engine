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

package insideworld.engine.actions.chain.execute.sequence;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.chain.execute.TestChainTags;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.Iterator;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

/**
 * Test of sequence execution of link.
 * @since 0.14.0
 */
@QuarkusTest
class TestSequence {

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
    TestSequence(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC:
     * Add several link to specific sequence and check that in LinkedList were added right values.
     * ER:
     * List should contain 1,2,3.
     * 4 shouldn't present because link should skip.
     * 5 shouldn't present because chain should break.
     */
    @Test
    final void test() {
        final Context context = this.executor.createContext();
        context.put(TestChainTags.LIST, Lists.newLinkedList());
        this.executor.execute(TestAction.class, context);
        final Collection<Integer> list = context.get(TestChainTags.LIST);
        assert list.size() == 3;
        final Iterator<Integer> iterator = list.iterator();
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 1; idx < 4; idx++) {
            assert iterator.next() == idx;
        }
    }

}
