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

package insideworld.engine.core.security.core.auth;


import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.ExecutorTags;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.AbstractExecuteWrapper;
import insideworld.engine.core.endpoint.base.action.EndpointProfile;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.security.core.SecurityException;
import insideworld.engine.core.security.core.action.RoleAction;
import insideworld.engine.core.security.core.data.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Role execution wrapper.
 * Check that in context exists user, and it's role is available.
 * @since 0.7.0
 */
@Dependent
public class AuthExecuteWrapper extends AbstractExecuteWrapper {

    /**
     * Map with auths grouped by profiles.
     */
    private final Map<Class<? extends EndpointProfile>, List<Auth>> auths;

    /**
     * Constructor.
     * @param auths Lists of auths.
     */
    @Inject
    public AuthExecuteWrapper(final List<Auth> auths) {
        this.auths = auths.stream().collect(Collectors.groupingBy(Auth::forProfile));
    }

    @Override
    public final void execute(final ExecuteContext context)
        throws CommonException {
        final RoleAction<?, ?> casted = this.castAction(context);
        final User user = this.executeAuth(context);
        if (user.getAvailableRoles().stream().noneMatch(casted.role()::contains)) {
            throw new SecurityException("Sorry dude, but you can't execute this action...");
        }
        super.execute(context);
    }

    @Override
    public final long wrapperOrder() {
        return 200_000;
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return List.copyOf(this.auths.keySet());
    }

    /**
     * Check and cast action to role type.
     * @param context Context.
     * @return Cast action.
     * @throws SecurityException Can't cast.
     */
    private RoleAction<?, ?> castAction(final ExecuteContext context) throws SecurityException {
        try {
            return (RoleAction<?, ?>) context.get(ExecutorTags.ACTION);
        } catch (final ClassCastException exp) {
            throw new SecurityException(
                "Role engine include to your app. Simple actions is prohibit. Please use RoleAction class"
            );
        }
    }

    private User executeAuth(final ExecuteContext context) throws SecurityException {
        User user = null;
        for (final Auth auth : this.auths.get(context.get(ExecutorTags.PROFILE))) {
            user = auth.auth(context);
            if (user != null) {
                break;
            }
        }
        if (user == null) {
            throw new SecurityException(
                "Can't find auth type for profile %s",
                context.get(ExecutorTags.PROFILE).getName()
            );
        }
        return user;
    }
}
