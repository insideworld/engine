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

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Write entities link.
 * Using to call write method in storage for specific entity or entities from context.
 * This is link required to init. See setTag and count method comments.
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
     * @param storages Storage keeper.
     */
    @Inject
    public WriteEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        if (this.single != null && context.contains(this.single)) {
            try {
                context.put(
                    this.single, this.writeSingle(context.get(this.single)), true
                );
            } catch (final StorageException exp) {
                throw new ActionException(context, exp);
            }
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.single.getTag());
                record.put("count", 1);
            }
        }
        if (this.multiple != null && context.contains(this.multiple)) {
            try {
                context.put(
                    this.multiple, this.writeMultiple(context.get(this.multiple)), true
                );
            } catch (final StorageException exp) {
                throw new ActionException(context, exp);
            }
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.multiple.getTag());
                record.put("count", context.get(this.multiple).size());
            }
        }
    }

    /**
     * Set tag of entity which need to write.
     * @param tag Entity tag.
     * @return The same instance.
     */
    public WriteEntityLink<T> setTag(final EntityTag<T> tag) {
        this.single = tag;
        return this;
    }

    /**
     * Set tag of entities which need to write.
     * @param tag Entities tag.
     * @return The same instance.
     */
    public WriteEntityLink<T> setTag(final EntitiesTag<T> tag) {
        this.multiple = tag;
        return this;
    }

    /**
     * Create a record in output with count of written entities.
     * @return The same instance.
     */
    public WriteEntityLink<T> setCount() {
        this.count = true;
        return this;
    }

    /**
     * Write single entity.
     * @param entity Entity.
     * @return Persisted entity.
     * @throws StorageException Can't write entity.
     */
    private T writeSingle(final T entity) throws StorageException {
        final Storage<T> storage = (Storage<T>) this.storages.getStorage(entity.getClass());
        return storage.write(entity);
    }

    /**
     * Write entities.
     * @param collection Collection of entities.
     * @return Persisted entities.
     * @throws StorageException Can't write entities.
     */
    private Collection<T> writeMultiple(final Collection<T> collection)
        throws StorageException {
        final Collection<T> results;
        final Optional<T> entity = collection.stream().findAny();
        if (entity.isPresent()) {
            final Storage<T> storage =
                (Storage<T>) this.storages.getStorage(entity.get().getClass());
            results = storage.writeAll(collection);
        } else {
            results = Collections.emptyList();
        }
        return results;
    }

}
