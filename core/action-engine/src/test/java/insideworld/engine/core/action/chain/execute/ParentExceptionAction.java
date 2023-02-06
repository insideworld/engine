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

import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.action.executor.key.ClassKey;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Parent class to test exception case.
 *
 * @since 0.14.0
 */
@Singleton
public class ParentExceptionAction extends AbstractChainAction<Void, Void, Void> {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    @Inject
    public ParentExceptionAction(final LinksBuilder<Void> builder) {
        super(builder);
    }

    @Override
    public String key() {
        return "insideworld.engine.core.action.chain.execute.ParentExceptionAction";
    }

    @Override
    public final void types(final Void input, final Void output) {
        //Nothing to do
    }

    @Override
    protected Collection<Link<? super Void>> attachLinks(final LinksBuilder<Void> builder)
        throws LinkException {
        return builder
            .addLink(new TypeLiteral<ExecuteActionLink<Void, Void, Void>>() {},
                link -> link.setAction(new ClassKey<>(ChildExceptionAction.class))
            ).build();
    }

    @Override
    protected Void aux(final Void input) {
        return input;
    }

    @Override
    protected Void output(final Void aux) {
        return aux;
    }
}
