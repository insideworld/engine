package insideworld.engine.actions.quarkus.beans;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.facade.ActionChanger;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Startup(3000)
@Singleton
public class ActionsInit {

    private final Collection<Action> actions;
    private final Collection<ActionChanger> changers;
    private final ObjectFactory factory;

    @Inject
    public ActionsInit(final Collection<Action> actions,
                       final Collection<ActionChanger> changers,
                       final ObjectFactory factory) {
        this.actions = actions;
        this.changers = changers;
        this.factory = factory;
    }

    @PostConstruct
    public void init() throws ActionException {
        for (final ActionChanger changer : this.changers) {
            changer.addActions(this.actions);
        }
    }
}
