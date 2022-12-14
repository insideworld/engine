/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.chain;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import java.util.Collection;

/**
 * Execute chain of links.
 */
public abstract class AbstractChainAction implements Action {

    private final Collection<Link> links;

    public AbstractChainAction(final LinksBuilder builder) {
        this.links = this.attachLinks(builder);
    }

    @Override
    public void execute(final Context context, final Output output) throws ActionException {
        for (final Link link : this.links) {
            if (context.contains(ActionsTags.BREAK_CHAIN)) {
                break;
            }
            if (link.can(context)) {
                link.process(context, output);
            }
        }
    }

    /**
     * Collection of links.
     * For build it use LinkBuilder
     * @return
     */
    protected abstract Collection<Link> attachLinks(LinksBuilder builder);

}
