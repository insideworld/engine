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

package insideworld.engine.core.jobs.core;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.Input;
import insideworld.engine.core.action.executor.key.StringKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.base.action.serializer.ActionSerializer;
import insideworld.engine.core.jobs.core.entity.JobEntity;
import java.io.InputStream;
import java.sql.SQLException;
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
    private final ActionExecutor executor;
    private final ActionSerializer serializer;

    @Inject
    public ActionJobExecutor(
        final ActionExecutor executor,
        final ActionSerializer serializer
    ) {
        this.executor = executor;
        this.serializer = serializer;
    }

    @Override
    public void execute(final JobExecutionContext execution) throws JobExecutionException {
        final JobEntity entity = (JobEntity) execution.getJobDetail().getJobDataMap().get("entity");
        try {
            this.execute(entity);
        } catch (final CommonException | SQLException exp) {
            LOGGER.trace("Finish execute job for alias {}", entity.getAlias());
            throw new JobExecutionException(
                String.format("Job for alias failed %s", entity.getAlias()),
                exp
            );
        }
    }

    private <I, O> void execute(final JobEntity entity) throws SQLException, CommonException {
        LOGGER.trace("Start execute job for alias {}", entity.getAlias());
        final StringKey<I, O> key = new StringKey<>(entity.getAction());
        final InputStream stream = entity.getInput().getAsciiStream();
        this.executor.execute(
            key,
            (Input<I>) () -> this.serializer.deserialize(key, stream)
        );
    }
}
