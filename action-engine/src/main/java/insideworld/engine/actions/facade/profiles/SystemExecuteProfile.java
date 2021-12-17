package insideworld.engine.actions.facade.profiles;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.keeper.context.Context;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SystemExecuteProfile implements ExecuteProfile {

    private final DefaultExecuteProfile def;
    private final Collection<PreExecutor> executors;

    @Inject
    public SystemExecuteProfile(final DefaultExecuteProfile def,
                                final Collection<PreExecutor> executors) {
        this.def = def;
        this.executors = executors.stream()
            .filter(executor -> executor.forProfile().contains(SystemExecuteProfile.class))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void preExecute(final Context context) throws ActionException {
        for (final PreExecutor executor : this.executors) {
            executor.preExecute(context);
        }
        this.def.preExecute(context);
    }
}
