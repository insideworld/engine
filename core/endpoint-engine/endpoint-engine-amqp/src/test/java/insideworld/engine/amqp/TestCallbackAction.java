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

package insideworld.engine.amqp;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import java.util.Map;
import java.util.Set;
import javax.inject.Singleton;
import org.testcontainers.shaded.com.google.common.collect.Sets;

/**
 * Action to test received information.
 * @since 1.0.0
 */
@Singleton
public class TestCallbackAction implements Action {

    /**
     * Concurent map to keep result from callback.
     */
    private final Set<String> set = Sets.newConcurrentHashSet();

    @Override
    public final void execute(final Context context, final Output output) throws CommonException {
        this.set.add(context.get("testValue"));
    }

    /**
     * Get received values.
     * @return Map with values.
     */
    public final Set<String> values() {
        return this.set;
    }

    @Override
    public final String key() {
        return "insideworld.engine.amqp.TestCallbackAction";
    }
}