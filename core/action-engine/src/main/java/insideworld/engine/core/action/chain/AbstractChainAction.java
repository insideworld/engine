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

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Collection;

/**
 * Abstract class for provide functionality to work with chain.
 * All contexts and outputs propagated from link to link.
 * How to add you links see JavaDoc for attachLinks method.
 * You may break a chain add to context BREAK_CHAIN tag.
 *
 * @param <I> Input data.
 * @param <O> Output data.
 * @param <A> Aux type using to move data between links.
 * @since 0.1.0
 */
public abstract class AbstractChainAction<I, O> implements Action<I, O> {

    /**
     * Link builder.
     */
    private final LinksBuilder<I> builder;
    private final ObjectFactory factory;

    /**
     * Collections of links.
     */
    private Collection<Link<? super I>> links;

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    public AbstractChainAction(final LinksBuilder<I> builder,
                               final ObjectFactory factory) {
        this.builder = builder;
        this.factory = factory;
    }

    @Override
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})
    public O execute(I input) throws CommonException {
        for (final var link : this.links) {
            if (link.can(input)) {
                //@checkstyle IllegalCatchCheck (10 lines)
                try {
                   if (!link.process(input)) {
                       break;
                   }
                } catch (final Exception exp) {
                    throw CommonException.wrap(
                        exp,
                        () -> new LinkException(link, exp),
                        LinkException.class
                    );
                }
            }
        }
        return this.output (input);
    }

    @Override
    public final void init() throws CommonException {
        if (this.links == null) {
            this.links = this.attachLinks(this.builder);
        } else {
            throw new ActionException(this, "Chain action already init!");
        }
    }

    /**
     * You need implement method attachLinks in inherited class and add you links use LinkBuilder.
     * Links will execute successively in order will you add through LinkBuilder.
     * How to use link builder see JavaDoc for related interface.
     *
     * @param builder LinksBuilder instance.
     * @return Collection of links.
     * @throws LinkException Exception at link init.
     * @see LinksBuilder
     * @checkstyle HiddenFieldCheck (2 lines)
     */
    protected abstract Collection<Link<? super I>> attachLinks(LinksBuilder<I> builder)
        throws LinkException;

    protected abstract O output(I input);
}
