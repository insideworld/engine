package insideworld.engine.actions.quarkus.beans;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.facade.OnStartupAction;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.facade.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup(3500)
@Singleton
public class LaunchStartupAction {

    private final ClassActionExecutor executor;
    private final Collection<Action> actions;
    private final ObjectFactory factory;

    @Inject
    public LaunchStartupAction(final ClassActionExecutor executor,
                               final Collection<Action> actions,
                               final ObjectFactory factory) {
        this.executor = executor;
        this.actions = actions;
        this.factory = factory;
    }

    @PostConstruct
    public void init() throws ActionException {
        for (final Action action : this.actions) {
            if (action.getClass().isAnnotationPresent(OnStartupAction.class)) {
                this.executor.execute(
                    action.getClass(),
                    this.factory.createObject(Context.class),
                    SystemExecuteProfile.class
                );
            }
        }
    }
}
