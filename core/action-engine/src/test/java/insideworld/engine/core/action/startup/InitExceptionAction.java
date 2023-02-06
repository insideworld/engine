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

package insideworld.engine.core.action.startup;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;

/**
 * Action to raise unhandled exception on init.
 * @since 0.14.0
 */
public class InitExceptionAction implements Action<Void, Void> {

    @Override
    public final Void execute(final Void input) throws CommonException {
        return null;
    }

    @Override
    public final void init() throws CommonException {
        throw new IllegalArgumentException("Unhandled");
    }

    @Override
    public final String key() {
        return "insideworld.engine.actions.startup.InitExceptionAction";
    }

    @Override
    public void types(final Void input, final Void output) {
        //Nothing to do
    }
}
