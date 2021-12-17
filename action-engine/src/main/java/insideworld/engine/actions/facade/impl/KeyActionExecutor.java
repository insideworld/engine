package insideworld.engine.actions.facade.impl;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Execute action by string key.
 *
 * @since 0.0.6
 */
@Singleton
public class KeyActionExecutor extends AbstractActionExecutor<String> {

    @Inject
    public KeyActionExecutor(final ObjectFactory factory,
                             final Collection<ExecuteProfile> profiles) {
        super(factory, profiles);
    }

    @Override
    protected String defineKey(final Action action) {
        return action.key();
    }
}
