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

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
    private final ActionExecutor executor;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     */
    @Inject
    TestKeyAction(final ActionExecutor executor) {
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
        this.executeAction(new ClassKey<>(ParentClassAction.class));
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
        this.executeAction(new ClassKey<>(ParentStringAction.class));
    }

//    /**
//     * TC: Execute action where not set an action key in link.
//     * ER: Should throw exception.
//     */
//    @Test
//    final void testNull() {
//        final var exception = Assertions.assertThrows(
//            ActionException.class,
//            () -> this.executeAction(ParentNullAction.class),
//            "Expected exception at init"
//        );
//        MatcherAssert.assertThat(
//            "Exception message wrong",
//            exception,
//            ExceptionMatchers.messageMatcher(
//                1, Matchers.containsString("Action is not set!")
//            )
//        );
//    }

    /**
     * TC: Check propagation exception from child action to parent.
     * ER: After 3 cause should be unhandled exception.
     */
    @Test
    final void testException() {
        MatcherAssert.assertThat(
            "Exception message wrong",
            () -> this.executor.execute(new ClassKey<>(ParentExceptionAction.class), null),
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
    private void executeAction(final Key<UUID, TestAux> action)
        throws CommonException {
        final UUID uuid = UUID.randomUUID();
        MatcherAssert.assertThat(
            "Context has incorrect values",
            this.executor.execute(action, uuid),
            Matchers.allOf(
                Matchers.hasProperty("UUID", Matchers.is(uuid)),
                Matchers.hasProperty("additional", Matchers.is("ChildAction"))
            )
        );
    }
}
