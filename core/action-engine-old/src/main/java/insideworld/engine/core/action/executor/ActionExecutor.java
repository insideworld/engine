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

package insideworld.engine.core.action.executor;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.profiles.ExecuteProfile;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;

/**
 * Interface to provide ability to execute an action.
 * @param <T> Type of action selector.
 * @since 0.0.1
 */
public interface ActionExecutor<T> {

    /**
     * Execute specific action by parameter.
     * @param parameter Parameter for define an action.
     * @param context Input context.
     * @return Result of action.
     * @throws ActionException Something went wrong.
     */
    Output execute(T parameter, Context context) throws ActionException;

    /**
     * Execute action in internal mechanism like jobs, preinit, etc.
     * @param parameter Action parameter.
     * @param context Context parameter.
     * @param profile Execute with specific profile.
     * @return Result of action.
     * @throws ActionException Something went wrong.
     */
    Output execute(T parameter, Context context, Class<? extends ExecuteProfile> profile)
        throws ActionException;

    /**
     * Create a context.
     * @return Context instance.
     */
    Context createContext();

}
