package insideworld.engine.jobs.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import insideworld.engine.actions.facade.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.jobs.common.entity.JobEntity;

import io.quarkus.runtime.StartupEvent;
import java.util.Map;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.Scheduler;

@Singleton
public class ActionJobs implements Jobs {
    private final Storage<JobEntity> storage;
    private final Scheduler quartz;

    private final ActionExecutor<String> executor;

    @Inject
    public ActionJobs(final Storage<JobEntity> storage,
                      final Scheduler quartz,
                      final ActionExecutor<String> executor) {
        this.storage = storage;
        this.quartz = quartz;
        this.executor = executor;
    }

    public void initJobs(final @Observes StartupEvent event)
        throws SchedulerException, StorageException, JsonProcessingException {
        var mapper = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
        for (final JobEntity entity : this.storage.readAll()) {
            final Context context = this.executor.createContext();
            context.values().putAll(mapper.readValue(entity.getContext()));
            final JobDetail job = JobBuilder.newJob(ActionJobExecutor.class)
                .withIdentity(entity.getAlias())
                .setJobData(new JobDataMap(
                    ImmutableMap.of(
                        "context", context,
                        "entity", entity
                    )
                ))
                .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(entity.getAlias())
                .startNow()
                .withSchedule(
                    SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
            this.quartz.scheduleJob(job, trigger);
        }
    }

    @Override
    public void start(final String alias) {
//        JobDetail job = JobBuilder.newJob(MyJob.class).withIdentity(alias).build();
//
//        SimpleTrigger build = TriggerBuilder.newTrigger()
//            .withIdentity(alias)
//            .withSchedule( SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(10)
//                .repeatForever())
//            .build();
//        quartz.scheduleJob()
//        quartz.pauseJob(job, SimpleTrigger);
//        quartz
    }

    @Override
    public void stop(final String alias) {

    }

    @Override
    public void launchAll() {

    }


//    private Map<String, Context> fillContext(final Collection<JobEntity> all, final ObjectFactory factory) {
//        var mapper = new ObjectMapper()
//            .readerFor(Map.class)
//            .with(DeserializationFeature.USE_LONG_FOR_INTS);
//        final ConcurrentHashMap<String, Context> result = new ConcurrentHashMap<>(all.size());
//        for (final JobEntity job : all) {
//            final Context context = factory.createObject(Context.class);
//            try {
//                if (StringUtils.isNotEmpty(job.getContext())) {
//                    context.values().putAll(mapper.readValue(job.getContext()));
//                }
//            } catch (final JsonProcessingException exp) {
//                throw new RuntimeException(exp);
//            }
//            result.put(job.getAlias(), context);
//        }
//        return result;
//    }
}
