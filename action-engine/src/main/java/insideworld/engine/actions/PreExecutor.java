package insideworld.engine.actions;

import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import java.util.Collection;

public interface PreExecutor {

    void preExecute(final Context context) throws ActionException;

    Collection<Class<? extends ExecuteProfile>> forProfile();
}
