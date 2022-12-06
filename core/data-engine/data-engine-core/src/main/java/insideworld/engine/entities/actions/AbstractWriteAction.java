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

package insideworld.engine.entities.actions;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.actions.links.WriteEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.converter.ImportEntityLink;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

/**
 * Abstract action to write entity.
 * This class is necessary to fast creation of typical actions to write entity.
 * Entity should come to this action in raw representation.
 * First they import to context, then write and after that - exported.
 * Context arguments:
 * Raw entity.
 * Output results:
 * Raw entity.
 * @param <T> Type of entity.
 * @see ImportEntityLink
 * @see ExportEntityLink
 * @since 0.0.1
 */
public abstract class AbstractWriteAction<T extends Entity> extends AbstractChainAction {

    /**
     * Default constructor.
     * @param builder Link builder.
     */
    public AbstractWriteAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder)
        throws LinkException {
        builder.addLink(ImportEntityLink.class, link -> link.setTag(this.getTag(), this.getType()));
        this.afterImport(builder);
        builder.addLink(
            new TypeLiteral<WriteEntityLink<T>>() { },
            link -> link.setTag(this.getTag())
        );
        builder.addLink(ExportEntityLink.class, link -> link.setTag(this.getTag()));
        this.afterExport(builder);
        return builder.build();
    }

    /**
     * Tag for place entity in context.
     * @return Entity tag.
     */
    protected abstract EntityTag<T> getTag();

    /**
     * Type of entity.
     * Using to define storage.
     * @return Type of entity.
     */
    protected abstract Class<T> getType();

    /**
     * Perform some operation after import entity.
     * @param builder Link builder.
     * @throws LinkException Link init exception.
     */
    protected abstract void afterImport(LinksBuilder builder) throws LinkException;

    /**
     * Perform some operation after export entity.
     * @param builder Link builder.
     * @throws LinkException Link init exception.
     */
    protected abstract void afterExport(LinksBuilder builder) throws LinkException;

}
