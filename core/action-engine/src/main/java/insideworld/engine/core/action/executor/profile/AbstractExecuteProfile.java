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

package insideworld.engine.core.action.executor.profile;

import com.google.common.collect.Queues;
import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.profile.wrapper.ExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.keeper.context.Context;
import insideworld.engine.core.common.startup.OnStartUp;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for executor profile.
 * Using to collect several profiles in one and activate it on startup.
 * @since 0.14.0
 */
public abstract class AbstractExecuteProfile implements ExecuteProfile {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExecuteProfile.class);

    /**
     * Filtered wrappers for giving profiles.
     */
    private final List<ExecuteWrapper> wrappers;


    /**
     * Default constructor.
     * @param executors Collection of all executors in the system.
     */
    @Inject
    public AbstractExecuteProfile(final List<ExecuteWrapper> executors) {
        this.wrappers = executors.stream()
            .filter(executor -> !Collections.disjoint(executor.forProfile(), this.profiles()))
            .sorted(Comparator.comparingLong(ExecuteWrapper::wrapperOrder).reversed())
            .toList();
        this.logOrder();
    }

    @Override
    public final void execute(final ExecuteContext context) throws CommonException {
        final Queue<ExecuteWrapper> stack = Queues.newArrayDeque(this.wrappers);
        stack.poll().execute(context, stack);
    }

    /**
     * For which profiles create this chain.
     * Usualy need to include here default profile and custom.
     * @return Profiles collection.
     */
    protected abstract Collection<Class<? extends ExecuteProfile>> profiles();

    /**
     * Log order of wrappers.
     */
    private void logOrder() {
        if (AbstractExecuteProfile.LOGGER.isDebugEnabled()) {
            AbstractExecuteProfile.LOGGER.debug(
                "Wrappers order for profile {}",
                this.getClass().getName()
            );
            for (final ExecuteWrapper wrapper : this.wrappers) {
                AbstractExecuteProfile.LOGGER.debug(
                    "{} {}", wrapper.wrapperOrder(), wrapper.getClass().getName()
                );
            }
        }
    }

}
