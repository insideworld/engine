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
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import java.util.function.Supplier;

public final class ExecutorTags {

    public static final ExecutorTag<Class<? extends ExecuteProfile>> PROFILE =
        new ExecutorTag<>("insideworld.engine.core.action.executor.ExecutorTags.PROFILE");

    public static final ExecutorTag<Input<?>> INPUT_PREDICATE =
        new ExecutorTag<>("insideworld.engine.core.action.executor.ExecutorTags.INPUT_PREDICATE");

    public static final ExecutorTag<Object> INPUT =
        new ExecutorTag<>("insideworld.engine.core.action.executor.ExecutorTags.INPUT");

    public static final ExecutorTag<Object> OUTPUT =
        new ExecutorTag<>("insideworld.engine.core.action.executor.ExecutorTags.OUTPUT");

    public static final ExecutorTag<Action<?,?>> ACTION =
        new ExecutorTag<>("insideworld.engine.core.action.executor.ExecutorTags.ACTION");

    private ExecutorTags() {
        //Nothing to do.
    }
}
