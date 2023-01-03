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

package insideworld.engine.core.action.chain;

import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.common.exception.CommonException;

/**
 * Link of chain.
 * Use separate functionality of actions and reuse code.
 *
 * @param <I> Input data.
 * @param <O> Output data.
 * @param <A> Auxdata.
 * @since 0.0.1
 */
public interface Link<I, O, A> {

    /**
     * Execute logic of link.
     * @param context Context.
     * @param output Output.
     * @throws CommonException Common exception during process action.
     */
    void process(I input, O output, A aux) throws CommonException;

    /**
     * Can execute this link.
     * @param context Context.
     * @return If return true - link will executed, if false - skip this link.
     */
    default boolean can(I input, A aux) {
        return true;
    }

}