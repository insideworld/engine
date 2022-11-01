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

package insideworld.engine.actions.executor.profiles;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;

/**
 * Abstract class for execute wrapper.
 * How to use it - you need to extend this class and override execute method.
 * In the execute method need to add call of super method to call next wrapper.
 * Because this class has state need to create non singleton beans.
 * @since 0.14.0
 */
public abstract class AbstractExecuteWrapper implements ExecuteWrapper {

    /**
     * Next wrapper.
     */
    private ExecuteWrapper next;

    /**
     * Method for extension.
     * Mandatory call the super method to keep chain.
     * @param action Action instance.
     * @param context Context.
     * @param output Output instance.
     * @throws ActionException Action exception.
     */
    public void execute(final Action action, final Context context, final Output output)
        throws ActionException {
        if (this.next != null) {
            this.next.execute(action, context, output);
        }
    }

    @Override
    public final void setNext(final ExecuteWrapper wrapper) {
        this.next = wrapper;
    }
}
