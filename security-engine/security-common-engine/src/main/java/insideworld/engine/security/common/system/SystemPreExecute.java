package insideworld.engine.security.common.system;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.facade.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Using for preauth with system user to launch system action.
 */
@Singleton
public class SystemPreExecute implements PreExecutor {

    private final User user;

    @Inject
    public SystemPreExecute(final PropertiesProvider properties,
                            final UserStorage storage) throws PropertiesException {
        final String username = properties.provide("engine.system.username", String.class);
        this.user = storage.getByName(username)
            .orElseThrow(() -> new PropertiesException("Can't find system user"));
    }

    @Override
    public void preExecute(final Context context) throws ActionException {
        context.put(UserTags.USER, this.user);
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(SystemExecuteProfile.class);
    }
}
