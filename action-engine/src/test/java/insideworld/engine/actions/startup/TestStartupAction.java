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

package insideworld.engine.actions.startup;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.OnStartupAction;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import javax.inject.Singleton;

/**
 * Startup action for test.
 * Set inited value to true on the launch.
 * After that should set in context - true.
 * @since 0.14.0
 */
@OnStartupAction
@Singleton
public class TestStartupAction implements Action {

    /**
     * Variable to check that action was executed before.
     */
    private Boolean inited;

    @Override
    public final void execute(final Context context, final Output output) throws CommonException {
        context.put("inited", this.inited);
        if (this.inited == null) {
            this.inited = true;
        }
    }

    @Override
    public final String key() {
        return "insideworld.engine.actions.startup.TestStartupAction";
    }
}
