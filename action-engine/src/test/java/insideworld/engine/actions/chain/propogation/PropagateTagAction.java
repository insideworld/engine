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

package insideworld.engine.actions.chain.propogation;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.actions.chain.TestChainTags;
import insideworld.engine.actions.chain.execute.ExecuteActionLink;
import insideworld.engine.exception.CommonException;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

/**
 * Action to test propagate tags to child action.
 * @since 0.14.0
 */
@Singleton
class PropagateTagAction extends AbstractChainAction {
    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    PropagateTagAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.chain.execute.conditions.ParentAction";
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder)
        throws CommonException {
        return builder.addLink(
            new TypeLiteral<ExecuteActionLink<Class<? extends Action>>>() { },
            link -> link.setKey(ChildAction.class).setTags(TestChainTags.ONE)
        ).build();
    }
}
