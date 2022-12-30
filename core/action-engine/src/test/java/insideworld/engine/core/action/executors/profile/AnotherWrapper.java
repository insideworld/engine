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
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.AbstractExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.context.Dependent;

/**
 * Test wrapper for the system profile.
 * Counting executed actions.
 * @since 0.14.0
 */
@Dependent
class AnotherWrapper extends AbstractExecuteWrapper {

    private final TestObject testObject;

    public AnotherWrapper(final TestObject testObject) {
        this.testObject = testObject;
    }

    @Override
    public final void execute(final ExecuteContext context)
        throws CommonException {
        this.testObject.counter.incrementAndGet();
        super.execute(context);
    }

    @Override
    public final long wrapperOrder() {
        return 10;
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(AnotherExecuteProfile.class);
    }
}
