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

package insideworld.engine.data.generator.inmemory.storage.abstracts;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import insideworld.engine.data.generator.inmemory.entity.abstracts.MemoryEntity;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractMemoryStorage<T extends Entity> implements Storage<T> {

    private final Map<Long, T> map = Maps.newConcurrentMap();
    private final ObjectFactory factory;
    private final Class<T> entity;

    public AbstractMemoryStorage(final ObjectFactory factory) {
        final ParameterizedType generics =
            (ParameterizedType) this.getClass().getGenericSuperclass();
        final Type[] arguments = generics.getActualTypeArguments();
        this.entity = (Class<T>) arguments[0];
        this.factory = factory;
    }

    @Override
    public Collection<T> readAll() throws StorageException {
        return this.map.values();
    }

    @Override
    public T read(long id) throws StorageException {
        if (!this.map.containsKey(id)) {
            final T entity = this.factory.createObject(forEntity());
            ((MemoryEntity) entity).setId(id);
            this.write(entity);
        }
        return this.map.get(id);
    }

    @Override
    public Collection<T> read(final Collection<Long> collection) throws StorageException {
        final Set<Long> ids = Sets.newHashSet(collection);
        return this.map.entrySet().stream()
            .filter(entry -> ids.contains(entry.getKey()))
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());
    }

    @Override
    public T write(T entity) {
        this.map.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Collection<T> writeAll(final Collection<T> collection) {
        collection.forEach(this::write);
        return collection;
    }

    @Override
    public void delete(final Collection<T> collection) {
        collection.forEach(entity -> this.map.remove(entity.getId()));
    }

    public Class<? extends T> forEntity() {
        return this.entity;
    }
}
