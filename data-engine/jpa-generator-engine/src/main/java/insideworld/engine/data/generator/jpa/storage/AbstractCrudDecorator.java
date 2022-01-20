package insideworld.engine.data.generator.jpa.storage;

import insideworld.engine.data.jpa.AbstractCrudStorage;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import java.util.Collection;

/**
 * Using this class to decorate adittional functionality for generated storage.
 * @param <T> Entity type.
 */
public abstract class AbstractCrudDecorator<T extends Entity> implements Storage<T> {

    protected final AbstractCrudStorage<T, ? extends T> storage;

    public AbstractCrudDecorator(final AbstractCrudStorage<T, ? extends T> storage) {
        this.storage = storage;
    }

    @Override
    public Collection<T> readAll() throws StorageException {
        return this.storage.readAll();
    }

    @Override
    public T read(final long id) throws StorageException {
        return this.storage.read(id);
    }

    @Override
    public Collection<T> read(final Collection<Long> ids) throws StorageException {
        return this.storage.read(ids);
    }

    @Override
    public T write(T entity) {
        return this.storage.write(entity);
    }

    @Override
    public Collection<T> writeAll(final Collection<T> entity) {
        return this.storage.writeAll(entity);
    }

    @Override
    public void delete(final Collection<T> entities) {
        this.storage.delete(entities);
    }

    @Override
    public boolean exists(final long id) {
        return this.storage.exists(id);
    }

    @Override
    public Class<? extends T> forEntity() {
        return this.storage.forEntity();
    }
}
