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

package insideworld.engine.core.action.chain.builder;

import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.TestChainTags;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import java.util.Objects;
import javax.enterprise.context.Dependent;

/**
 * Link to test TypeLiteral.
 * Just add the string tag to context.
 * @since 0.14.0
 */
@Dependent
class StringLink implements GenericLink<String> {

    /**
     * String value.
     */
    private String string;

    @Override
    public final void process(final Context context, final Output output) throws LinkException {
        context.put(TestChainTags.STRING, Objects.requireNonNullElse(this.string, "2"));
    }

    @Override
    public final void setValue(final String value) {
        this.string = value;
    }
}