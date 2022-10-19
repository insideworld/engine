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
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Read an entity by presents tags.
 *
 * Need to set one or both tags - for multifple and single entity. Also need to set a type.
 * In case if ID won't present in context:
 *  - For single tag just skip filling.
 *  - For multiple tag will read all entities.
 *
 *  All results will put in context.
 *
 * @since 0.0.1
 */
@Dependent
public class ReadEntityLink<T extends Entity> implements Link {

    private final StorageKeeper storages;
    private Storage<T> storage;
    private Pair<SingleTag<Long>, EntityTag<T>> single;
    private Pair<MultipleTag<Long>, EntitiesTag<T>> multiple;

    /**
     * Constructor.
     * @param storages Storage keeper instance.
     */
    @Inject
    public ReadEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.single != null && context.contains(this.single.getLeft())) {
            context.put(
                this.single.getRight(),
                this.processSingle(context.get(this.single.getLeft()))
            );
        } else {
            if (this.multiple != null) {
                context.put(
                    this.multiple.getRight(),
                    this.processMultiple(context.get(this.multiple.getLeft()))
                );
            }
        }
    }

    /**
     * Read a single entity by id.
     * @param id Id of entity.
     * @return Entity.
     */
    private T processSingle(final Long id) throws ActionException {
        try {
            return this.storage.read(id);
        } catch (final StorageException exp) {
            throw new ActionException("Can't read an entity", exp);
        }
    }

    /**
     * Read entities by ids.
     * If ids is empty - read all entities.
     *
     * @param ids Ids of entities.
     * @return Collection of entities.
     */
    private Collection<T> processMultiple(final Collection<Long> ids) throws ActionException {
        try {
            final Collection<T> collection;
            if (CollectionUtils.isEmpty(ids)) {
                collection = this.storage.readAll();
            } else {
                collection = this.storage.read(ids);
            }
            return collection;
        } catch (final StorageException exp) {
            throw new ActionException("Can't read an entity", exp);
        }
    }

    /**
     * Set single tag to read.
     * @param read ID tag with entity ID.
     * @param put Entity tag to put in context.
     * @return The same instance.
     */
    public ReadEntityLink<T> setTag(final SingleTag<Long> read, final EntityTag<T> put) {
        if (read != null && put != null) {
            this.single = Pair.of(read, put);
        }
        return this;
    }

    /**
     * Set multiple tag to read.
     * @param read ID's tag with entity ID's.
     * @param put Entities tag to put in context.
     * @return The same instance.
     */
    public ReadEntityLink<T> setTags(final MultipleTag<Long> read, final EntitiesTag<T> put) {
        if (read != null && put != null) {
            this.multiple = Pair.of(read, put);
        }
        return this;
    }

    /**
     * Set type of entity.
     * @param type Entity type.
     * @return The same instance
     */
    public ReadEntityLink<T> setType(final Class<T> type) {
        try {
            this.storage = this.storages.getStorage(type);
        } catch (final StorageException exp) {
            throw new ActionRuntimeException(new ActionException("Can't find storage", exp));
        }
        return this;
    }
}
