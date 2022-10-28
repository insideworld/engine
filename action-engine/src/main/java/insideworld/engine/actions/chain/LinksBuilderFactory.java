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

package insideworld.engine.actions.chain;

import com.google.common.collect.ImmutableList;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.function.Consumer;
import javax.enterprise.context.Dependent;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

/**
 * Build links using object factory.
 *
 * @since 0.0.6
 */
@Dependent
public class LinksBuilderFactory implements LinksBuilder {

    /**
     * Object factor.
     */
    private final ObjectFactory factory;

    /**
     * Collections of links.
     */
    private final ImmutableList.Builder<Link> links = ImmutableList.builder();

    /**
     * Default constructor.
     * @param factory Object factory.
     */
    @Inject
    public LinksBuilderFactory(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public final LinksBuilder addLink(final Class<? extends Link> type) {
        final Link link = this.factory.createObject(type);
        this.links.add(link);
        return this;
    }

    @Override
    public final  <T extends Link> LinksBuilder addLink(final Class<T> type, final Consumer<T> init) {
        final T link = this.factory.createObject(type);
        init.accept(link);
        this.links.add(link);
        return this;
    }

    @Override
    public final LinksBuilder addLink(final TypeLiteral<? extends Link> type) {
        final Link link = this.factory.createObject(type);
        this.links.add(link);
        return this;
    }

    @Override
    public final  <T extends Link> LinksBuilder addLink(
        final TypeLiteral<T> type, final Consumer<T> init) {
        final T link = this.factory.createObject(type);
        init.accept(link);
        this.links.add(link);
        return this;
    }

    @Override
    public final Collection<Link> build() {
        return this.links.build();
    }
}
