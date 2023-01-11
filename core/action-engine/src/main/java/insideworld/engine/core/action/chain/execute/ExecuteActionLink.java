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

package insideworld.engine.core.action.chain.execute;

import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Abstract logic for execute action inside chain action.
 * Need to extend realisation to support different key types.
 *
 * @since 0.1.0
 */
@Dependent
public class ExecuteActionLink<I, O, A> implements Link<A> {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;
    private final ActionExecutor executor;
    private BiFunction<A, ObjectFactory, I> input;
    private BiConsumer<A, O> output;
    private Key<I, O> key;
    private Consumer<ExecuteContext> context;

    /**
     * Default constructor.
     *
     * @param executor Action executor.
     * @param factory Object factory.
     */
    @Inject
    public ExecuteActionLink(final ObjectFactory factory, final ActionExecutor executor) {
        this.factory = factory;
        this.executor = executor;
    }

    @Override
    public final boolean process(final A aux) throws CommonException {
        final O out;
        if (this.context == null) {
            out = this.executor.execute(this.key, this.getInput(aux));
        } else {
            out = this.executor.execute(this.key, this.getInput(aux), this.context);
        }
        if (this.output != null) {
            this.output.accept(aux, out);
        }
        return true;
    }

    private I getInput(final A aux) {
        final I result;
        if (this.input == null) {
            result = null;
        } else {
            result = this.input.apply(aux, this.factory);
        }
        return result;
    }

    public ExecuteActionLink<I, O, A> setInput(final BiFunction<A, ObjectFactory, I> input) {
        this.input = input;
        return this;
    }

    public ExecuteActionLink<I, O, A> setOutput(final BiConsumer<A, O> output) {
        this.output = output;
        return this;
    }

    public ExecuteActionLink<I, O, A> setAction(final Key<I, O> key) {
        this.key = key;
        return this;
    }

    public ExecuteActionLink<I, O, A> setContext(final Consumer<ExecuteContext> context) {
        this.context = context;
        return this;
    }
}
