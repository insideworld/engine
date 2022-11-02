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

package insideworld.engine.actions.executors.key;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executors.TestExecutorTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import javax.inject.Singleton;

/**
 * Just raise the exception depended on INTEGER tag.
 * @since 0.14.0
 */
@Singleton
class ExceptionAction implements Action {

    @Override
    public final void execute(final Context context, final Output output) throws ActionException {
        switch (context.get(TestExecutorTags.EXCEPTION)) {
            case 1:
                throw new ActionException("With message without exception");
            case 2:
                throw new ActionException(context, new IllegalArgumentException("With context"));
            case 3:
                throw new ActionException(
                    "With message and exception", new IllegalArgumentException("With context")
                );
            default:
                throw new IllegalArgumentException("And what I should do?");
        }
    }

    @Override
    public final String key() {
        return "insideworld.engine.actions.executors.key.ExceptionAction";
    }
}
