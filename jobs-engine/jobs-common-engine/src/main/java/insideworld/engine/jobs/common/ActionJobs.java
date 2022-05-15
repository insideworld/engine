package insideworld.engine.jobs.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.facade.ActionChanger;
import insideworld.engine.actions.facade.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.jobs.common.entity.JobEntity;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.Scheduler;

@Singleton
@Startup(4000)
public class ActionJobs implements Jobs {
    private final Storage<JobEntity> storage;
    private final Scheduler quartz;

    @Inject
    public ActionJobs(final Storage<JobEntity> storage,
                      final Scheduler quartz) {
        this.storage = storage;
        this.quartz = quartz;
    }

    @PostConstruct
    public void init() throws SchedulerException, StorageException {
        for (final JobEntity entity : this.storage.readAll()) {
            final JobDetail job = JobBuilder.newJob(ActionJobExecutor.class)
                .withIdentity(entity.getAlias())
                .setJobData(new JobDataMap(ImmutableMap.of("entity", entity)))
                .build();
            final Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(entity.getAlias())
                .withSchedule(CronScheduleBuilder.cronSchedule(entity.getExpression()))
                .build();
            this.quartz.scheduleJob(job, trigger);
            this.stop(entity.getAlias());
        }
        launchAll();
    }

    @Override
    public void start(final String alias) {
        try {
            this.quartz.resumeJob(JobKey.jobKey(alias));
        } catch (final SchedulerException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public void stop(final String alias) {
        try {
            this.quartz.pauseJob(JobKey.jobKey(alias));
        } catch (final SchedulerException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public void launchAll() {
        try {
            this.storage.readAll().stream().filter(entity -> !entity.isManual()).forEach(
                entity -> {
                    try {
                        this.quartz.resumeJob(JobKey.jobKey(entity.getAlias()));
                    } catch (final SchedulerException exp) {
                        throw new RuntimeException(exp);
                    }
                }
            );
        } catch (final StorageException exp) {
            throw new RuntimeException(exp);
        }
    }
}
