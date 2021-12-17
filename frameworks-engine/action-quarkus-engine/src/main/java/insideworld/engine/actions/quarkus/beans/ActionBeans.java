package insideworld.engine.actions.quarkus.beans;

import com.google.common.collect.ImmutableList;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.facade.ActionChanger;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.tags.MandatoryTag;
import insideworld.engine.quarkus.AbstractBeans;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;

public class ActionBeans extends AbstractBeans {

    @Produces
    public Collection<ExecuteProfile> executeProfiles(final Instance<ExecuteProfile> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<PreExecutor> executors(final Instance<PreExecutor> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<Link> links(final Instance<Link> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<Action> actions(final Instance<Action> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<ActionChanger> actionChangers(final Instance<ActionChanger> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<MandatoryTag> mandatoryTags(final Instance<MandatoryTag> instance) {
        return this.fromInstance(instance);
    }
}
