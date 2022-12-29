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

package insideworld.engine.core.action.chain.execute;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.chain.TestChainTags;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
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
 * Execute action by specific key without additional arguments.
 * See test method comments.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestKeyAction {

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
    TestKeyAction(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
    }

    /**
     * TC: Execute action from chain action and add only action parameter using class key.
     * ER: In child action should add to output UUID from parent context and add one more record
     * to test merge.
     *
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
     *
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
        final var exception = Assertions.assertThrows(
            ActionException.class,
            () -> this.executeAction(ParentNullAction.class),
            "Expected exception at init"
        );
        MatcherAssert.assertThat(
            "Exception message wrong",
            exception,
            ExceptionMatchers.messageMatcher(
                1, Matchers.containsString("Action is not set!")
            )
        );
    }

    /**
     * TC: Check propagation exception from child action to parent.
     * ER: After 3 cause should be unhandled exception.
     */
    @Test
    final void testException() {
        MatcherAssert.assertThat(
            "Exception message wrong",
            () -> this.executeAction(ParentExceptionAction.class),
            ExceptionMatchers.catchException(
                ActionException.class,
                Matchers.allOf(
                    ExceptionMatchers.messageMatcher(
                        3, Matchers.containsString("Unhandled")
                    ),
                    Matchers.hasProperty("indexes", Matchers.iterableWithSize(5))
                )
            )
        );
    }

    /**
     * Just execute action and make compare by UUID and Additional tag.
     *
     * @param action Test action to execute.
     * @throws ActionException Exception.
     */
    private void executeAction(final Class<? extends Action> action) throws ActionException {
        final Context context = this.executor.createContext();
        final UUID uuid = UUID.randomUUID();
        context.put(TestChainTags.UUID, uuid);
        MatcherAssert.assertThat(
            "Context has incorrect values",
            this.executor.execute(action, context),
            Matchers.hasItems(
                Matchers
                    .both(KeeperMatchers.contain(TestChainTags.UUID))
                    .and(
                        KeeperMatchers.match(
                            TestChainTags.UUID, Matchers.is(uuid)
                        )
                    ),
                Matchers
                    .both(KeeperMatchers.contain(TestChainTags.OUTPUT_ADDITIONAL))
                    .and(
                        KeeperMatchers.match(
                            TestChainTags.OUTPUT_ADDITIONAL, Matchers.is("ChildAction")
                        )
                    )
            )
        );
    }
}
