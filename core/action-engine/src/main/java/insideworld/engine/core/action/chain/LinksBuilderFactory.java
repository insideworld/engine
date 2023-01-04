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

import com.google.common.collect.ImmutableList;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

/**
 * Build links using object factory.
 *
 * @since 0.0.6
 */
@Dependent
public class LinksBuilderFactory<I> implements LinksBuilder<I> {

    /**
     * Object factor.
     */
    private final ObjectFactory factory;

    /**
     * List builder with links.
     */
    private final ImmutableList.Builder<Link<? super I>> links;

    /**
     * Default constructor.
     *
     * @param factory Object factory.
     */
    @Inject
    public LinksBuilderFactory(final ObjectFactory factory) {
        this.factory = factory;
        this.links = ImmutableList.builder();
    }

    @Override
    public final LinksBuilder<I> addLink(final Class<? extends Link<? super I>> type) {
        this.links.add(this.factory.createObject(type));
        return this;
    }

    @Override
    public final <T extends Link<? super I>> LinksBuilder<I> addLink(
        final Class<T> type, final LinkConsumer<I, T> init
    ) throws LinkException {
        final T link = this.factory.createObject(type);
        this.initLink(link, init);
        this.links.add(link);
        return this;
    }

    @Override
    public final <T extends Link<? super I>> LinksBuilder<I> addLink(final TypeLiteral<T> type) {
        this.links.add(this.factory.createObject(type));
        return this;
    }

    @Override
    public final <T extends Link<? super I>> LinksBuilder<I> addLink(
        final TypeLiteral<T> type, final LinkConsumer<I, T> init
    ) throws LinkException {
        final T link = this.factory.createObject(type);
        this.initLink(link, init);
        this.links.add(link);
        return this;
    }

    /**
     * Init link and catch exception to init.
     *
     * @param link Link.
     * @param init Init consumer.
     * @param <T> Type of link.
     * @throws LinkException Exception at init.
     */
    @SuppressWarnings({"PMD.AvoidCatchingGenericException"})
    private <T extends Link<? super I>> void initLink(
        final T link, final LinkConsumer<? super I, T> init
    )
        throws LinkException {
        //@checkstyle IllegalCatchCheck (10 lines)
        try {
            init.init(link);
        } catch (final Exception exp) {
            throw CommonException.wrap(
                exp,
                () -> new LinkException(link, exp),
                LinkException.class
            );
        }
    }

    @Override
    public Collection<Link<? super I>> build() {
        return this.links.build();
    }
}
