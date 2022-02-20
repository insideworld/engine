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
