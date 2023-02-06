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
import insideworld.engine.core.common.exception.CommonException;

/**
 * Action interface.
 * Using to implement different actions to execute it throw ActionExecutor.
 * @see insideworld.engine.core.action.executor.ActionExecutor
 * @param <I> Input type.
 * @param <O> Output type.
 * @since 0.0.1
 */
public interface Action<I, O> {

    /**
     * Action logic need to implement here.
     * @param input Input data.
     * @return Output of action.
     * @throws CommonException Exception on action processing.
     */
    O execute(I input) throws CommonException;

    /**
     * Make some inits before call action.
     * @throws CommonException Exception on action init.
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

    /**
     * This method is using in different mechanism which need to get a type of input and output
     * For example it may be serializers and etc.
     * No need to set logic here because all information will take from reflection.
     * IMPORTANT:
     * 1) Implement this method only in final class.
     * 2) Don't use it abstracts classes and don't use any generic here.
     * 3) Don't overload this method.
     * @param input Input.
     * @param output Output.
     */
    void types(I input, O output);
}
