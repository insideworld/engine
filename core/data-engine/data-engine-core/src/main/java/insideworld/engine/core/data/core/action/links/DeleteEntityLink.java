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

package insideworld.engine.core.data.core.action.links;

import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
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
public class DeleteEntityLink<T extends Entity, I> implements Link<I> {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Storage for this link.
     */
    private Storage<T> storage;

    private Function<I, T> single;

    private Function<I, Collection<T>> multiple;

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
    public final boolean process(final I input)
        throws LinkException, StorageException {
        if (this.single == null && this.multiple == null || this.storage == null) {
            throw new LinkException(this, "Link was not init");
        }
        if (this.single != null) {
            final T entity = this.single.apply(input);
            if (single != null) {
                this.storage.delete(Collections.singleton(entity));
            }
        }
        if (this.multiple != null) {
            final Collection<T> entities = this.multiple.apply(input);
            if (CollectionUtils.isNotEmpty(entities)) {
                this.storage.delete(entities);
            }
        }
        return true;
    }

    /**
     * Set single tag to delete.
     * Will call delete for entity keeping under this tag.
     *
     * @param psingle Single entity tag.
     * @return The same instance.
     */
    public DeleteEntityLink<T, I> setSingle(final Function<I, T> psingle) {
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
    public final DeleteEntityLink<T, I> setMultiple(final Function<I, Collection<T>> pmultiple) {
        this.multiple = pmultiple;
        return this;
    }

    /**
     * Set type of entity.
     * It's necessary to define which storage need to use.
     *
     * @param type Entity type.
     * @return The same instance
     * @throws StorageException If storage can't find for provided type.
     */
    public final DeleteEntityLink<T, I> setType(final Class<T> type) throws StorageException {
        this.storage = this.storages.getStorage(type);
        return this;
    }
}
