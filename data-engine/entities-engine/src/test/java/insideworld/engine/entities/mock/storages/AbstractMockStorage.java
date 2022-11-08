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

package insideworld.engine.entities.mock.storages;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract hash map storage for mocks.
 * @param <T> Entity type.
 * @since 0.14.0
 */
public abstract class AbstractMockStorage<T extends Entity> implements Storage<T> {

    /**
     * Collection of entities.
     */
    private final Map<Long, T> entities;

    /**
     * Count of entity.
     */
    private volatile AtomicLong count;

    /**
     * Default constructor.
     */
    protected AbstractMockStorage() {
        this.entities = new HashMap<>();
        this.count = new AtomicLong(2);
    }

    @Override
    public final Collection<T> readAll() throws StorageException {
        return this.entities.values();
    }

    @Override
    public final T read(final long id) throws StorageException {
        return this.entities.get(id);
    }

    @Override
    public final T write(final T entity) {
        if (entity.getId() == 0) {
            try {
                final Method method = entity.getClass().getDeclaredMethod("setId", Long.class);
                method.invoke(entity, this.count.incrementAndGet());
            } catch (final
                NoSuchMethodException
                    | IllegalAccessException
                        | InvocationTargetException exp) {
                throw new ActionRuntimeException(new ActionException("Problem?", exp));
            }
        }
        this.entities.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public final Collection<T> writeAll(final Collection<T> entity) {
        return entity.stream().map(this::write).toList();
    }

    @Override
    public final void delete(final Collection<T> pentities) {
        pentities.forEach(ent -> this.entities.remove(ent.getId()));
    }

    @Override
    public final boolean exists(final long id) {
        return this.entities.containsKey(id);
    }

    @Override
    public final Collection<T> read(final Collection<Long> ids) throws StorageException {
        return ids.stream().map(this.entities::get).toList();
    }
}
