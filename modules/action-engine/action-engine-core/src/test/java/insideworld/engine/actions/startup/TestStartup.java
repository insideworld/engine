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

package insideworld.engine.actions.startup;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.test.KeeperMatchers;
import insideworld.engine.exception.CommonException;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import insideworld.engine.startup.StartUpException;
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
    private final ActionExecutor<Class<? extends Action>> executor;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     */
    @Inject
    TestStartup(final ActionExecutor<Class<? extends Action>> executor) {
        this.executor = executor;
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
        final Context context = this.executor.createContext();
        this.executor.execute(TestStartupAction.class, context);
        MatcherAssert.assertThat(
            context,
            KeeperMatchers.match("inited", Matchers.is(true))
        );
    }

    /**
     * TC:
     * Try to raise handled and unhandled exception trough init.
     * ER:
     * Should be right exceptions type and messages.
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
                StartUpException.class,
                Matchers.allOf(
                    ExceptionMatchers.classMatcher(1, ActionException.class),
                    ExceptionMatchers.classMatcher(2, IllegalArgumentException.class),
                    ExceptionMatchers.messageMatcher(
                        2,
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
                StartUpException.class,
                Matchers.allOf(
                    ExceptionMatchers.classMatcher(1, ActionException.class),
                    ExceptionMatchers.messageMatcher(
                        1,
                        Matchers.containsString("Handled")
                    )
                )
            )
        );
    }
}
