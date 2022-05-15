package insideworld.engine.jobs.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Maps;
import insideworld.engine.actions.facade.ActionExecutor;
import insideworld.engine.actions.facade.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.jobs.common.entity.JobEntity;
import java.util.Map;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ActionJobExecutor implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionJobExecutor.class);
    private ActionExecutor<String> executor;
    private Map<String, Context> contexts = Maps.newConcurrentMap();

    private final ObjectReader mapper = new ObjectMapper()
        .readerFor(Map.class)
        .with(DeserializationFeature.USE_LONG_FOR_INTS);

    @Inject
    public ActionJobExecutor(final ActionExecutor<String> executor) throws StorageException {
        this.executor = executor;
    }

    @Override
    public void execute(final JobExecutionContext execution) throws JobExecutionException {
        final JobEntity entity = (JobEntity) execution.getJobDetail().getJobDataMap().get("entity");
        LOGGER.trace("Start execute job for alias {}", entity.getAlias());
        final Context context = getContext(entity).clone();
        try {
            final Output output = this.executor.execute(
                entity.getKey(),
                context,
                SystemExecuteProfile.class);
        } catch (final Exception exp) {
            LOGGER.error(String.format("Job for alias failed %s", entity.getAlias()), exp);
            throw exp;
        }
        if (entity.isCacheContext()) {
            this.contexts.put(entity.getAlias(), context.clone());
        }
        LOGGER.trace("Finish execute job for alias {}", entity.getAlias());
    }

    private Context getContext(final JobEntity entity) {
        if (!this.contexts.containsKey(entity.getAlias())) {
            final Context context = this.executor.createContext();
            context.put(JobTags.JOB, entity);
            try {
                context.values().putAll(mapper.readValue(entity.getContext()));
            } catch (final JsonProcessingException exp) {
                throw new RuntimeException(exp);
            }
            this.contexts.put(entity.getAlias(), context);
        }
        return this.contexts.get(entity.getAlias());
    }
}
