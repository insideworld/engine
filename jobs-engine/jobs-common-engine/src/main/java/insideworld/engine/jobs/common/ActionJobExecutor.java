package insideworld.engine.jobs.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import insideworld.engine.actions.facade.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.jobs.common.entity.JobEntity;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Dependent
public class ActionJobExecutor implements Job {

    private ActionExecutor<String> executor;

    private Map<String, Context> contexts;

    @Inject
    public ActionJobExecutor(final ActionExecutor<String> executor,
                             final Storage<JobEntity> storage) throws StorageException {
        this.executor = executor;
        final var mapper = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
        this.contexts = storage.readAll().stream().collect(Collectors.toUnmodifiableMap(
            JobEntity::getAlias,
            job -> {
                final Context context = this.executor.createContext();
                try {
                    context.values().putAll(mapper.readValue(job.getContext()));
                } catch (final JsonProcessingException exp) {
                    throw new RuntimeException(exp);
                }
                return context;
            }
        ));
    }
    @Override
    public void execute(final JobExecutionContext execution) throws JobExecutionException {
        final JobEntity entity = (JobEntity) execution.getJobDetail().getJobDataMap().get("entity");
        System.out.println("qwe");
    }
}
