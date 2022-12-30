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

package insideworld.engine.core.action.executor.profile.wrapper;

import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.keeper.context.Context;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Interface to provide some logic before and after execute an action.
 * Implementation should contain call the execute method on a next action.
 * Work on all actions.
 * Implementation with order = 0 using to call action.
 * This mechanism help to wrap execute action for example to create a new transaction.
 * @see ExecuteProfile
 * @see insideworld.engine.core.action.executor.profile.DefaultExecuteProfile
 * @since 0.1.0
 */
public interface ExecuteWrapper {

    /**
     * Perform some function before or after action.
     * @param action Supplier with action.
     * @throws CommonException Some exception.
     */
    void execute(ExecuteContext context) throws CommonException;

    /**
     * Order of execute profile.
     * If positive - execute before action.
     * If negative - after action.
     * Zero is reserved for execute action and set up this value will be unpredictably order.
     * @return Order.
     */
    long wrapperOrder();

    /**
     * For which profile need to perform the implementation.
     * @return Collections of profiles.
     */
    Collection<Class<? extends ExecuteProfile>> forProfile();

    /**
     * Set next wrapper.
     * @param wrapper Next wrapper.
     */
    void setNext(ExecuteWrapper wrapper);
}
