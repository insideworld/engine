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

package insideworld.engine.actions.executors.profile;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.profiles.wrapper.AbstractExecuteWrapper;
import insideworld.engine.actions.executor.profiles.DefaultExecuteProfile;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.executor.profiles.wrapper.WrapperContext;
import insideworld.engine.actions.executors.TestExecutorTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.context.Dependent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrapper one.
 * Just add -1 to the Sequence list.
 * @since 0.14.0
 */
@Dependent
class WrapperNegative extends AbstractExecuteWrapper {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperNegative.class);

    @Override
    public final void execute(final WrapperContext context)
        throws CommonException {
        if (context.context().contains(TestExecutorTags.SEQUENCE)) {
            WrapperNegative.LOGGER.debug("Wrapper -1");
            context.context().get(TestExecutorTags.SEQUENCE).add(-1);
        }
        super.execute(context);
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
