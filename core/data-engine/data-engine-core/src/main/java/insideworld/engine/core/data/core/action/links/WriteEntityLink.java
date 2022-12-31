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
import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Write entities link.
 * Using to call write method in storage for specific entity or entities from context.
 * This is link required to init. See setTag and count method comments.
 *
 * @param <T> Entity type to delete.
 * @since 0.0.1
 */
@Dependent
public class WriteEntityLink<T extends Entity, I> implements Link<I> {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Tag for single entity.
     */
    private Pair<Function<I, T>, BiConsumer<I, T>> single;

    /**
     * Tag for multiple entity.
     */
    private Pair<Function<I, Collection<T>>, BiConsumer<I, Collection<T>>> multiple;

    private Storage<T> storage;

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
    public final boolean process(final I input) throws LinkException, StorageException {
        if (this.single == null && this.multiple == null) {
            throw new LinkException(this, "Link was not init");
        }
        //TODO: Complexity!
        if (this.single != null) {
            final T entity = this.single.getLeft().apply(input);
            if (entity != null) {
                final T write = this.storage.write(entity);
                Optional.of(this.single.getRight()).ifPresent(handler -> handler.accept(input, write));
            }
        }
        if (this.multiple != null) {
            final Collection<T> entity = this.multiple.getLeft().apply(input);
            if (entity != null) {
                final Collection<T> write = this.storage.writeAll(entity);
                Optional.of(this.multiple.getRight())
                    .ifPresent(handler -> handler.accept(input, write));
            }
        }
        return true;
    }

    /**
     * Set tag of entity which need to write.
     *
     *
     * @return The same instance.
     */
    public WriteEntityLink<T, I> setSingle(
        final Function<I, T> entity,
        final BiConsumer<I, T> handler
    ) {
        this.single = Pair.of(entity, handler);
        return this;
    }

    /**
     * Set tag of entities which need to write.
     *
     *
     * @return The same instance.
     */
    public WriteEntityLink<T, I> setMultiple(
        final Function<I, Collection<T>> entities,
        final BiConsumer<I, Collection<T>> handler
    ) {
        this.multiple = Pair.of(entities, handler);
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
    public final WriteEntityLink<T, I> setType(final Class<T> type) throws StorageException {
        this.storage = this.storages.getStorage(type);
        return this;
    }
}
