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

package insideworld.engine.entities.actions.links;

import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collections;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Delete entities link.
 * Using to call delete method in storage for specific entity or entities from context.
 * This is link required to init. See setTag, setTags and setType method comments.
 *
 * @param <T> Entity type to delete.
 * @since 0.0.1
 */
@Dependent
public class DeleteEntityLink<T extends Entity> implements Link {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Storage for this link.
     */
    private Storage<T> storage;

    /**
     * Tag for single entity.
     */
    private EntityTag<T> single;

    /**
     * Tag for collection of entitites.
     */
    private EntitiesTag<T> multiple;

    /**
     * Default constructor.
     *
     * @param storages Storage keeper.
     */
    @Inject
    public DeleteEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final void process(final Context context, final Output output) throws LinkException {
        if (this.single == null && this.multiple == null && this.storage == null) {
            throw new LinkException(this.getClass(), "Link was not init");
        }
        try {
            if (context.contains(this.single)) {
                this.storage.delete(Collections.singleton(context.get(this.single)));
            }
            if (context.contains(this.multiple)) {
                this.storage.delete(context.get(this.multiple));
            }
        } catch (final StorageException exp) {
            throw this.exception(exp);
        }
    }

    /**
     * Set single tag to delete.
     * Will call delete for entity keeping under this tag.
     *
     * @param psingle Single entity tag.
     * @return The same instance.
     */
    public DeleteEntityLink<T> setTag(final EntityTag<T> psingle) {
        this.single = psingle;
        return this;
    }

    /**
     * Set multiple tag to delete.
     * Will call delete for entities keeping under this tag.
     *
     * @param pmultiple Single entity tag.
     * @return The same instance.
     */
    public final DeleteEntityLink<T> setTags(final EntitiesTag<T> pmultiple) {
        this.multiple = pmultiple;
        return this;
    }

    /**
     * Set type of entity.
     * It's necessary to define which storage need to use.
     *
     * @param type Entity type.
     * @return The same instance
     * @throws LinkException If storage can't find for provided type.
     */
    public final DeleteEntityLink<T> setType(final Class<T> type) throws LinkException {
        try {
            this.storage = this.storages.getStorage(type);
        } catch (final StorageException exp) {
            throw this.exception(exp);
        }
        return this;
    }
}
