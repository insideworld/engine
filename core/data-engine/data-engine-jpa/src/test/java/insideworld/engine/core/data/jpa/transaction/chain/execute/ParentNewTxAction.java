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

package insideworld.engine.core.data.jpa.transaction.chain.execute;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.action.chain.execute.ExecuteActionLink;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import insideworld.engine.core.data.jpa.entities.TestTags;
import insideworld.engine.core.data.core.action.links.ReadEntityLink;
import insideworld.engine.core.data.core.tags.StorageTags;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

/**
 * Execute child action in new TX.
 * @since 0.14.0
 */
@Singleton
class ParentNewTxAction extends AbstractChainAction {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    ParentNewTxAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    public String key() {
        return "insideworld.engine.tests.unit.actions.chain.execute.tx.ParentNewTxAction";
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder) throws LinkException {
        return builder
            .addLink(
                new TypeLiteral<ReadEntityLink<SomeEntity>>() { },
                link -> link.setTag(StorageTags.ID, TestTags.SOME_ENTITY).setType(SomeEntity.class)
            )
            .addLink(
                new TypeLiteral<ExecuteActionLink<Class<? extends Action>>>() { },
                link -> link.setKey(ChildAction.class)
            )
            .addLink(ExceptionLink.class)
            .build();
    }
}
