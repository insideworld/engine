/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.security.common.action;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.facade.profiles.DefaultExecuteProfile;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.security.common.RoleException;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.entities.User;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class RolePreExecutor implements PreExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RolePreExecutor.class);

    @Override
    public void preExecute(final Context context) throws ActionException {
        final User user = context.get(UserTags.USER);
        final RoleAction action;
        try {
            action = (RoleAction) context.get(ActionsTags.ACTION);
        } catch (final ClassCastException exp) {
            throw new ActionException("Role engine include to your app. Simple actions is prohibit. Please use RoleAction class", exp);
        }
        if (user == null) {
            throw new RoleException("Can't find who execute this action");
        }
        LOGGER.trace("User {}", user.getName());
        if (user.getAvailableRoles().stream().noneMatch(action.role(context)::contains)) {
            throw new RoleException("Sorry dude, but you can't execute this action...");
        }
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(DefaultExecuteProfile.class);
    }
}
