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

import java.util.Collection;
import java.util.function.Consumer;
import javax.enterprise.util.TypeLiteral;

/**
 * This interface provide functionality to be able to add links to your chain action.
 * Call addLink method which need in your situation and after that call the build method.
 *
 * @see Link
 * @see AbstractChainAction
 * @since 0.1.0
 */
public interface LinksBuilder {

    /**
     * Add specific link implementation based on class type.
     * Usually using for singleton link instance.
     * @param type Class type of link.
     * @return The same instance.
     */
    LinksBuilder addLink(Class<? extends Link> type);

    /**
     * Add specific link implementation based on class type and configurate it.
     * Usually using for non singleton links which behavior depended on action.
     * @param type Class type of link.
     * @param init Init predicate which execute once at action init.
     * @return The same instance.
     * @param <T> Link type.
     */
    <T extends Link> LinksBuilder addLink(Class<T> type, Consumer<T> init);

    /**
     * Add specific link with generic parameters.
     * Usually using for singleton link instance with generic chain actions.
     * @param type Type literal. Example: new TypeLiteral<SomeGenericLink<T>>() {}
     * @return The same instance.
     */
    LinksBuilder addLink(TypeLiteral<? extends Link> type);

    /**
     * Add specific link with generic parameters and configurate it.
     * Usually using for non singleton links which behavior depended on generic actions.
     * @param type Type literal. Example: new TypeLiteral<SomeGenericLink<T>>() {}
     * @param init Init predicate which execute once at action init.
     * @return The same instance.
     * @param <T> Link type.
     */
    <T extends Link> LinksBuilder addLink(TypeLiteral<T> type, Consumer<T> init);

    /**
     * Build links collections.
     * Need to use when all links were added.
     * @return Collection of links.
     */
    Collection<Link> build();

}
