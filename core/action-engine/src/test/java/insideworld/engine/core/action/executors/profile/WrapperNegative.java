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

package insideworld.engine.core.action.executors.profile;

import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.profile.DefaultExecuteProfile;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.ExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.Stack;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper one.
 * Just add -1 to the Sequence list.
 *
 * @since 0.14.0
 */
@Singleton
class WrapperNegative implements ExecuteWrapper {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperNegative.class);
    private final TestObject test;

    @Inject
    public WrapperNegative(final TestObject test) {
        this.test = test;
    }

    @Override
    public final void execute(final ExecuteContext context, final Queue<ExecuteWrapper> wrappers)
        throws CommonException {
        WrapperNegative.LOGGER.debug("Wrapper -1");
        this.test.integers.add(-1);
        this.next(context, wrappers);
    }

    @Override
    public final long wrapperOrder() {
        return -1;
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(DefaultExecuteProfile.class);
    }
}
