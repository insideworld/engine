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

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.exception.CommonException;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import insideworld.engine.security.core.SecurityException;
import insideworld.engine.security.core.UserTags;
import insideworld.engine.security.core.entities.User;
import insideworld.engine.security.core.storages.UserStorage;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
class SecurityTest {

    private final ActionExecutor<Class<? extends Action>> executor;
    private final UserStorage<User> users;

    @Inject
    SecurityTest(
        final ActionExecutor<Class<? extends Action>> executor,
        final UserStorage<User> users
    ) {
        this.executor = executor;
        this.users = users;
    }

    /**
     * TC: Test execute actions with user ONE.
     * ER: Should be available to execute OneAction and BothAction.
     *  TwoAction should raise an exception.
     * @throws CommonException Shouldn't raise.
     */
    @Test
    final void testOne() throws CommonException {
        final Context context = this.executor.createContext();
        context.put(UserTags.USER, this.users.getByName("one").get());
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(OneAction.class, context.cloneContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(BothAction.class, context.cloneContext()),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception wasn't raised",
            () -> this.executor.execute(SystemAction.class, context.cloneContext()),
           ExceptionMatchers.catchException(ActionException.class)
        );
        MatcherAssert.assertThat(
            "Exception wasn't raised",
            () -> this.executor.execute(TwoAction.class, context.cloneContext()),
            ExceptionMatchers.catchException(ActionException.class)
        );
    }

    /**
     * TC: Execute action from system profile.
     * ER: Should execute any action except NoRoleAction.
     *
     */
    @Test
    final void systemProfileTest() {
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                SystemAction.class, this.executor.createContext(), SystemExecuteProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                OneAction.class, this.executor.createContext(), SystemExecuteProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                TwoAction.class, this.executor.createContext(), SystemExecuteProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception was raised",
            () -> this.executor.execute(
                BothAction.class, this.executor.createContext(), SystemExecuteProfile.class
            ),
            Matchers.not(ExceptionMatchers.catchException(ActionException.class))
        );
        MatcherAssert.assertThat(
            "Exception wasn't raised",
            () -> this.executor.execute(
                NoRoleAction.class, this.executor.createContext(), SystemExecuteProfile.class
            ),
            ExceptionMatchers.catchException(
                ActionException.class,
                Matchers.allOf(
                    ExceptionMatchers.classMatcher(1, SecurityException.class),
                    ExceptionMatchers.messageMatcher(
                        1,
                        Matchers.is("Sorry dude, but you can't execute this action...")
                    )
                )
            )
        );
    }
}
