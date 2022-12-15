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

package insideworld.engine.core.data.core.action;

import insideworld.engine.core.action.chain.AbstractChainAction;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.action.links.DeleteEntityLink;
import insideworld.engine.core.data.core.action.links.ReadEntityLink;
import insideworld.engine.core.data.core.tags.EntitiesTag;
import insideworld.engine.core.data.core.tags.EntityTag;
import insideworld.engine.core.data.core.tags.StorageTags;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

/**
 * Abstract action to delete entity by ID or IDS tags.
 * This class is necessary to fast creation of typical actions to delete entity.
 * Input arguments:
 * insideworld.engine.core.data.core.tags.StorageTags#ID - for delete only single entity.
 * or
 * insideworld.engine.core.data.core.tags.StorageTags#IDS - for delete several entities.
 * @param <T> Type of entity.
 * @since 0.0.1
 */
public abstract class AbstractDeleteAction<T extends Entity> extends AbstractChainAction {

    /**
     * Default constructor.
     * @param builder Link builder.
     */
    public AbstractDeleteAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected final Collection<Link> attachLinks(final LinksBuilder builder)
        throws LinkException {
        return builder
            .addLink(
                new TypeLiteral<ReadEntityLink<T>>() {
                },
                link -> link.setType(this.getType())
                    .setTag(StorageTags.ID, this.getTag())
                    .setTags(StorageTags.IDS, this.getTags()))
            .addLink(
                new TypeLiteral<DeleteEntityLink<T>>() {
                },
                link -> link
                    .setType(this.getType())
                    .setTag(this.getTag())
                    .setTags(this.getTags()))
            .build();
    }

    /**
     * Tag of entity which will using to keep it in context.
     * Return null here if you want to disable delete by id.
     * @return Entity tag.
     */
    protected abstract EntityTag<T> getTag();

    /**
     * Tag of entities which will using to keep it in context.
     * Return null here if you want to disable delete by ids.
     * @return Entities tag.
     */
    protected abstract EntitiesTag<T> getTags();

    /**
     * Type of entity.
     * @return Type of entity.
     */
    protected abstract Class<T> getType();
}