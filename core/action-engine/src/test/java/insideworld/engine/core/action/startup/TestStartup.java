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

package insideworld.engine.core.action.startup;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ClassActionExecutor;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collections;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test startup.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestStartup {

    /**
     * Class action executor.
     */
    private final ClassActionExecutor executor;
    private final TestStartupAction action;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     */
    @Inject
    TestStartup(final ClassActionExecutor executor,
                final TestStartupAction action
    ) {
        this.executor = executor;
        this.action = action;
    }

    /**
     * TC: Execute action which set in context a specific tag to validate that this action
     * was executed before.
     * ER:
     * Should present inited tag with true value.
     *
     * @throws CommonException Exception.
     */
    @Test
    final void test() throws CommonException {
        this.executor.execute(TestStartupAction.class, null);
        MatcherAssert.assertThat(
            this.action.init,
            Matchers.is(true)
        );
    }

    /**
     * TC:
     * Try to raise handled and unhandled exception trough init.
     * ER:
     * Should be right exceptions type and messages.
     *
     * @checkstyle NonStaticMethodCheck (10 lines)
     */
    @Test
    final void testException() {
        MatcherAssert.assertThat(
            "Unhandled exception",
            () -> new ActionsInit(
                Collections.singletonList(new InitExceptionAction()), null
            ).startUp(),
            ExceptionMatchers.catchException(
                ActionException.class,
                Matchers.allOf(
                    ExceptionMatchers.classMatcher(1, IllegalArgumentException.class),
                    ExceptionMatchers.messageMatcher(
                        1,
                        Matchers.containsString("Unhandled")
                    )
                )
            )
        );
        MatcherAssert.assertThat(
            "Handled exception",
            () -> new ActionsInit(
                Collections.singletonList(new InitActionExceptionAction()), null
            ).startUp(),
            ExceptionMatchers.catchException(
                ActionException.class,
                ExceptionMatchers.messageMatcher(
                    0, Matchers.containsString("Handled")
                )
            )
        );
    }
}
