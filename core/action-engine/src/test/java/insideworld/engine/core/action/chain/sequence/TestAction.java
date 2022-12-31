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

package insideworld.engine.core.action.chain.sequence;

import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Add all links in necessary sequence.
 *
 * @since 0.14.0
 */
@Singleton
class TestAction extends AbstractChainAction<Input, Integer[]> {

    /**
     * Default constructor.
     *
     * @param builder Links builder instance.
     */
    @Inject
    TestAction(final LinksBuilder<Input> builder) {
        super(builder);
    }

    @Override
    protected Collection<Link<? super Input>> attachLinks(final LinksBuilder<Input> builder)
        throws LinkException {
        return builder
            .addLink(LinkOne.class)
            .addLink(LinkTwo.class)
            .addLink(LinkThree.class)
            .addLink(LinkSkip.class)
            .addLink(LinkBreakChain.class)
            .addLink(LinkAfterBreakChain.class)
            .build();
    }

    @Override
    protected Integer[] output(Input input) {
        return input.integers.toArray(Integer[]::new);
    }

    @Override
    public final String key() {
        return "insideworld.engine.actions.chain.sequence.TestAction";
    }

    @Override
    public Class<Input> inputType() {
        return Input.class;
    }

    @Override
    public Class<Integer[]> outputType() {
        return Integer[].class;
    }


}
