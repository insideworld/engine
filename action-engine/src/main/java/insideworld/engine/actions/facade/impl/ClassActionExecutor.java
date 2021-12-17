package insideworld.engine.actions.facade.impl;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ClassActionExecutor extends AbstractActionExecutor<Class<? extends Action>> {

    @Inject
    public ClassActionExecutor(final ObjectFactory factory,
                               final Collection<ExecuteProfile> profiles) {
        super(factory, profiles);
    }

    @Override
    protected final Class<? extends Action> defineKey(final Action action) {
        return action.getClass();
    }
}
