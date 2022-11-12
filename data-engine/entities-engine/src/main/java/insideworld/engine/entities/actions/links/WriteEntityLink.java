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
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Write entities link.
 * Using to call write method in storage for specific entity or entities from context.
 * This is link required to init. See setTag and count method comments.
 *
 * @param <T> Entity type to delete.
 * @since 0.0.1
 */
@Dependent
public class WriteEntityLink<T extends Entity> implements Link {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Tag for single entity.
     */
    private EntityTag<T> single;

    /**
     * Tag for multiple entity.
     */
    private EntitiesTag<T> multiple;

    /**
     * Write count of write in output.
     * Looks like need to delete it in the future.
     */
    private boolean count;

    /**
     * Default constructor.
     *
     * @param storages Storage keeper.
     */
    @Inject
    public WriteEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final void process(final Context context, final Output output)
        throws LinkException, StorageException {
        if (this.single == null && this.multiple == null) {
            throw new LinkException(this.getClass(), "Link was not init");
        }
        context.put(
            this.single, this.writeSingle(context.get(this.single), output), true
        );
        context.put(
            this.multiple, this.writeMultiple(context.get(this.multiple), output), true
        );
    }

    /**
     * Set tag of entity which need to write.
     *
     * @param tag Entity tag.
     * @return The same instance.
     */
    public WriteEntityLink<T> setTag(final EntityTag<T> tag) {
        this.single = tag;
        return this;
    }

    /**
     * Set tag of entities which need to write.
     *
     * @param tag Entities tag.
     * @return The same instance.
     */
    public WriteEntityLink<T> setTag(final EntitiesTag<T> tag) {
        this.multiple = tag;
        return this;
    }

    /**
     * Create a record in output with count of written entities.
     *
     * @return The same instance.
     */
    public WriteEntityLink<T> setCount() {
        this.count = true;
        return this;
    }

    /**
     * Write single entity.
     *
     * @param entity Entity.
     * @param output Output.
     * @return Persisted entity or null.
     * @throws StorageException Can't write entity.
     */
    private T writeSingle(final T entity, final Output output) throws StorageException {
        final T result;
        if (entity == null) {
            result = null;
        } else {
            final Storage<T> storage = (Storage<T>) this.storages.getStorage(entity.getClass());
            result = storage.write(entity);
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.single.getTag());
                record.put("count", 1);
            }
        }
        return result;
    }

    /**
     * Write entities.
     *
     * @param collection Collection of entities.
     * @param output Output.
     * @return Persisted entities or empty collection.
     * @throws StorageException Can't write entities.
     */
    private Collection<T> writeMultiple(final Collection<T> collection, final Output output)
        throws StorageException {
        final Collection<T> result;
        if (CollectionUtils.isEmpty(collection)) {
            result = null;
        } else {
            final Storage<T> storage = (Storage<T>) this.storages.getStorage(
                collection.iterator().next().getClass()
            );
            result = storage.writeAll(collection);
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.multiple.getTag());
                record.put("count", result.size());
            }
        }
        return result;
    }

}
