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

package insideworld.engine.security.common.action;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.old.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import insideworld.engine.security.common.stubs.auth.TestProfile;
import insideworld.engine.core.security.core.UserTags;
import insideworld.engine.core.security.core.data.UserStorage;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SecurityTest {

    private final ActionExecutor executor;
    private final UserStorage users;

    @Inject
    SecurityTest(
        final ActionExecutor executor,
        final UserStorage users
    ) {
        this.executor = executor;
        this.users = users;
    }

    /**
     * TC: Test execute actions with user ONE for test endpoint profile.
     * ER: Should be available to execute OneAction and BothAction.
     * TwoAction should raise an exception.
     *
     * @throws CommonException Shouldn't raise.
     */
    @Test
    final void testOne() throws CommonException {
        context.put(UserTags.USER, this.users.getByName("one").get());
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                OneAction.class, context.cloneContext(), TestProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                BothAction.class, context.cloneContext(), TestProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception wasn't raised",
            () -> this.executor.execute(
                SystemAction.class, context.cloneContext(), TestProfile.class
            ),
            ExceptionMatchers.catchException(ActionException.class)
        );
        MatcherAssert.assertThat(
            "Exception wasn't raised",
            () -> this.executor.execute(
                TwoAction.class, context.cloneContext(), TestProfile.class
            ),
            ExceptionMatchers.catchException(ActionException.class)
        );
    }

    /**
     * TC: Execute action from default profile.
     * ER: Should execute any action.
     */
    @Test
    final void defaultProfileTest() {
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                SystemAction.class, this.executor.createContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                OneAction.class, this.executor.createContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(TwoAction.class, this.executor.createContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(BothAction.class, this.executor.createContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(NoRoleAction.class, this.executor.createContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
    }
}
