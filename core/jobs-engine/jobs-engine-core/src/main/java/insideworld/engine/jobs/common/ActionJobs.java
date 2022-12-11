/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.jobs.common;

import com.google.common.collect.ImmutableMap;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.jobs.common.entity.JobEntity;
import io.quarkus.runtime.Startup;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

@Singleton
@Startup(5000)
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
