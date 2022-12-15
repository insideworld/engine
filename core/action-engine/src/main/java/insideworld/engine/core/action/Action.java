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

package insideworld.engine.core.action;

import com.fasterxml.jackson.annotation.JsonGetter;
import insideworld.engine.core.action.executor.ActionChanger;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.common.exception.CommonException;

/**
 * Action interface.
 * To execute implemented action see implementation of ActionExecutor.
 * To register action see ActionChanger.
 * @see ActionExecutor
 * @see ActionChanger
 * @since 0.0.1
 */
public interface Action {

    /**
     * Execute an action.
     * @param context Context is using for input data.
     * @param output Output is using for store result of the action.
     * @throws CommonException Exception during action execution.
     * @see Context
     * @see Output
     */
    void execute(Context context, Output output) throws CommonException;

    /**
     * Make some inits before call action.
     * @throws CommonException Some exception at init.
     */
    default void init() throws CommonException {
        //By default - nothing to do.
    }

    /**
     * Key of action in string representation.
     * @return Key.
     */
    @JsonGetter
    String key();
}
