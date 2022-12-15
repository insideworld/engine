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
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.common.exception.CommonException;
import java.util.Collection;

/**
 * Abstract class for provide functionality to work with chain.
 * All contexts and outputs propagated from link to link.
 * How to add you links see JavaDoc for attachLinks method.
 * You may break a chain add to context BREAK_CHAIN tag.
 *
 * @since 0.1.0
 */
public abstract class AbstractChainAction implements Action {

    /**
     * Link builder.
     */
    private final LinksBuilder builder;

    /**
     * Collections of links.
     */
    private Collection<Link> links;

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    public AbstractChainAction(final LinksBuilder builder) {
        this.builder = builder;
    }

    @Override
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})
    public final void execute(final Context context, final Output output) throws CommonException {
        for (final Link link : this.links) {
            if (context.contains(ChainTags.BREAK_CHAIN)) {
                break;
            }
            if (link.can(context)) {
                //@checkstyle IllegalCatchCheck (10 lines)
                try {
                    link.process(context, output);
                } catch (final Exception exp) {
                    throw CommonException.wrap(
                        exp,
                        () -> new LinkException(link, exp),
                        LinkException.class
                    );
                }
            }
        }
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
    protected abstract Collection<Link> attachLinks(LinksBuilder builder) throws LinkException;

}
