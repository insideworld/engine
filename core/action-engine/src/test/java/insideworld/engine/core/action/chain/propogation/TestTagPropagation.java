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

package insideworld.engine.core.action.chain.propogation;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.chain.TestChainTags;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.common.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test tag propagation.
 * See test method comments.
 * @since 0.14.0
 */
@QuarkusTest
class TestTagPropagation {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     * @param executor Class action executor.
     */
    @Inject
    TestTagPropagation(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * Test tag propagation.
     * TC:
     * Execute child action with propagate only tag ONE.
     * ER: Output contains only one record with ONE tag.
     * @throws CommonException Exception.
     */
    @Test
    final void testTagPropagate() throws CommonException {
        final Context context = this.executor.createContext();
        context.put(TestChainTags.ONE, new Object());
        context.put(TestChainTags.TWO, new Object());
        MatcherAssert.assertThat(
            "Expected value in output record",
            this.executor.execute(PropagateTagAction.class, context),
            Matchers.hasItems(
                KeeperMatchers.contain(TestChainTags.ONE),
                Matchers.not(KeeperMatchers.contain(TestChainTags.TWO))
            )
        );
    }

}
