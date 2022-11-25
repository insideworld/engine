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

import com.google.common.collect.Lists;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    private final Map<Long, T> map;

    /**
     * Count of entity.
     */
    private volatile AtomicLong count;

    /**
     * Default constructor.
     */
    protected AbstractMockStorage() {
        this.map = new HashMap<>();
        this.count = new AtomicLong(2);
    }

    @Override
    public final Collection<T> readAll() throws StorageException {
        return this.map.values();
    }

    @Override
    public final T read(final long id) throws StorageException {
        return this.map.get(id);
    }

    @Override
    public final T write(final T entity) throws StorageException {
        if (entity.getId() == 0) {
            try {
                final Method method = entity.getClass().getDeclaredMethod("setId", Long.class);
                method.invoke(entity, this.count.incrementAndGet());
            } catch (final
                NoSuchMethodException
                    | IllegalAccessException
                        | InvocationTargetException exp) {
                throw new StorageException(exp, "Problem?");
            }
        }
        this.map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public final Collection<T> writeAll(final Collection<T> entities) throws StorageException {
        final List<T> result = Lists.newArrayListWithCapacity(entities.size());
        for (final T entity : entities) {
            result.add(this.write(entity));
        }
        return result;
    }

    @Override
    public final void delete(final Collection<T> pentities) {
        pentities.forEach(ent -> this.map.remove(ent.getId()));
    }

    @Override
    public final boolean exists(final long id) {
        return this.map.containsKey(id);
    }

    @Override
    public final Collection<T> read(final Collection<Long> ids) throws StorageException {
        return ids.stream().map(this.map::get).toList();
    }
}
