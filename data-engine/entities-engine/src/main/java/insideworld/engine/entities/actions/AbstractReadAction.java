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
import insideworld.engine.entities.actions.links.ReadEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.entities.tags.StorageTags;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

/**
 * Abstract action to read entity by ID or IDS tags.
 * This class is necessary to fast creation of typical actions to read entity.
 * Input arguments:
 * insideworld.engine.entities.tags.StorageTags#ID - for read only single entity.
 * or
 * insideworld.engine.entities.tags.StorageTags#IDS - for read several entities.
 * All read entity or entities will add to output using ExportEntityLink.
 *
 * @param <T> Type of entity.
 * @see ExportEntityLink
 * @since 0.0.1
 */
public abstract class AbstractReadAction<T extends Entity> extends AbstractChainAction {

    /**
     * Default constructor.
     *
     * @param builder Link builder.
     */
    public AbstractReadAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder) throws LinkException {
        builder
            .addLink(
                new TypeLiteral<ReadEntityLink<T>>() { },
                link -> link.setType(this.getType())
                    .setTag(StorageTags.ID, this.getTag())
                    .setTags(StorageTags.IDS, this.getTags()))
            .addLink(
                ExportEntityLink.class,
                link -> link
                    .setTag(this.getTag())
                    .setTag(this.getTags()));
        this.afterExport(builder);
        return builder.build();
    }

    /**
     * Tag which using to store entity in context.
     * Return null here if you want to disable read by id.
     *
     * @return Entity tag.
     */
    protected abstract EntityTag<T> getTag();

    /**
     * Tag which using to store entities in context.
     * Return null here if you want to disable read by ids.
     *
     * @return Entity tag.
     */
    protected abstract EntitiesTag<T> getTags();

    /**
     * Type of entity.
     * Using to define storage.
     * @return Type of entity.
     */
    protected abstract Class<T> getType();

    /**
     * Some operations which need to make after export entities.
     * @param builder Link builder.
     */
    protected abstract void afterExport(LinksBuilder builder);

}
