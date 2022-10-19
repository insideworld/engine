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

package insideworld.engine.actions.facade;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.actions.facade.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;


/**
 * Using for launch action.
 *
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
    Output execute(T parameter, Context context) throws ActionRuntimeException;

    /**
     * Execute action in internal mechanism like jobs, preinit and etc.
     * @param parameter Action parameter.
     * @param context Context parameter.
     * @param profile Execute with specific profile.
     * @return
     * @throws ActionRuntimeException
     */
    Output execute(T parameter, Context context, Class<? extends ExecuteProfile> profile)
        throws ActionRuntimeException;

    /**
     * Create a context.
     * @return Context instance.
     */
    Context createContext();

}
