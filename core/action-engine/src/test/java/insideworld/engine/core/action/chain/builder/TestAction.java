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

import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Collection;
import java.util.UUID;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

/**
 * Test action.
 * Add 4 links to test different methods to add link.
 *
 * @since 0.14.0
 */
@Singleton
class TestAction extends AbstractChainAction<Input, Input, Input> {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    public TestAction(final LinksBuilder<Input> builder) {
        super(builder);
    }

    @Override
    public final Class<Input> inputType() {
        return Input.class;
    }

    @Override
    public final Class<Input> outputType() {
        return Input.class;
    }

    @Override
    public final String key() {
        return "insideworld.engine.core.action.chain.builder.TestAction";
    }

    @Override
    protected final Collection<Link<? super Input>> attachLinks(final LinksBuilder<Input> builder)
        throws LinkException {
        return builder
            .addLink(IntegerLink.class)
            .addLink(new TypeLiteral<GenericLink<String>>() { })
            .addLink(InitLink.class, link -> link.setUuid(UUID.randomUUID()))
            .addLink(
                new TypeLiteral<GenericLink<UUID>>() { },
                link -> link.setValue(UUID.randomUUID())
            ).build();
    }

    @Override
    protected final Input aux(final Input input) {
        return input;
    }

    @Override
    protected final Input output(final Input input) {
        return input;
    }

}
