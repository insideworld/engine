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

package insideworld.engine.actions.chain.execute.builder;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import java.util.Collection;
import java.util.UUID;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

/**
 * Test action.
 * Add 4 links to test different methods to add link.
 * @since 0.14.0
 */
@Singleton
class TestAction extends AbstractChainAction {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    TestAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    public final String key() {
        return "insideworld.engine.actions.chain.execute.builder.TestAction";
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder) {
        return builder
            .addLink(IntegerLink.class)
            .addLink(new TypeLiteral<GenericLink<String>>() { })
            .addLink(InitLink.class, link -> link.setUuid(UUID.randomUUID()))
            .addLink(
                new TypeLiteral<GenericLink<UUID>>() { },
                link -> link.setValue(UUID.randomUUID())
            ).build();
    }
}
