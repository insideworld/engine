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

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.keeper.Record;
import insideworld.engine.core.common.keeper.context.Context;

/**
 * Provide ability to execute action using key of action.
 * @since 2.0.0
 */
public interface KeyActionExecutor extends ActionExecutor<String> {

    /**
     * Execute action with default parameters.
     * @param key
     * @param input
     * @return
     * @param <I>
     * @param <O>
     * @throws CommonException
     */
    <I,O> O execute(String key, I input) throws CommonException;

    /**
     * Execute action by a key.
     * @param key Key of action.
     * @param input Input.
     * @return Output.
     * @param <I> Input type.
     * @param <O> Output type.
     * @throws CommonException Exception during action execution.
     */
    <I,O> O execute(String key, I input, ExecuteContext context) throws CommonException;

}
