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
