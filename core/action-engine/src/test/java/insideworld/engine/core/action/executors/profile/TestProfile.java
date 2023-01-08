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
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.ExecutorTags;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test execution of profiles.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestProfile {

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
    TestProfile(final ActionExecutor executor,
                final TestObject test) {
        this.executor = executor;
        this.test = test;
    }

    /**
     * TC:
     * Add to default profile
     * Add to system profile pre executor with increment counter in test object.
     * ER:
     * Default executor profile shouldn't increment counter.
     * System executor profile should increment counter.
     * All profiles shouldn't execute NotBoundWrapper.
     *
     * @throws CommonException Exception.
     */
    @Test
    final void testPreExecutor() throws CommonException {
        this.test.counter.set(0);
        this.executor.execute(new ClassKey<>(DummyAction.class), null);
        MatcherAssert.assertThat(
            "Counter was incremented",
            this.test.counter.get(),
            Matchers.is(0L)
        );
        this.executor.execute(
            new ClassKey<>(DummyAction.class),
            null,
            context -> context.put(ExecutorTags.PROFILE, AnotherExecuteProfile.class)
        );
        MatcherAssert.assertThat(
            "Counter wasn't incremented",
            this.test.counter.get(),
            Matchers.is(1L)
        );
    }

}
