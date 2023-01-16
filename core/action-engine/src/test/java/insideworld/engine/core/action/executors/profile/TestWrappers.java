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

package insideworld.engine.core.action.executors.profile;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test wrappers and they execution sequence.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestWrappers {

    /**
     * Class action executor.
     */
    private final ActionExecutor executor;
    private final TestObject test;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     */
    @Inject
    TestWrappers(final ActionExecutor executor,
                 final TestObject test) {
        this.executor = executor;
        this.test = test;
    }

    /**
     * TC:
     * Create LinkedList for check order of executed wrappers.
     * Create 4 wrappers with order from 3 to -1 where 0 is the execute action wrapper.
     * After that execute dummy action.
     * ER:
     * Sequence should contain values from 3 to -1 with right order.
     *
     * @throws CommonException Exception.
     */
    @Test
    final void test() throws CommonException {
        this.test.integers.clear();
        this.executor.execute(new ClassKey<>(DummyAction.class));
        MatcherAssert.assertThat(
            "Collection has wrong sequence",
            this.test.integers,
            Matchers
                .both(
                    Matchers.<Integer>iterableWithSize(5)
                )
                .and(
                    Matchers.containsInRelativeOrder(
                        Matchers.is(3),
                        Matchers.is(2),
                        Matchers.is(1),
                        Matchers.is(0),
                        Matchers.is(-1)
                    )
                )
        );
    }
}
