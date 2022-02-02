package insideworld.engine.data.jpa;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class AbstractCrudStorage<T extends Entity, C extends T>
    implements Storage<T>, PanacheRepository<C> {

    @Override
    public Collection<T> readAll() {
        return this.castUpper(findAll().list());
    }

    @Override
    public T read(final long id) throws StorageException {
        return findByIdOptional(id)
            .orElseThrow(() ->
                new StorageException(String.format("%s not found by ID %s", this.forEntity(), id)));
    }

    @Override
    public Collection<T> read(final Collection<Long> ids) {
        return this.castUpper(find("id in (?1)", ids).list());
    }

    @Override
    public T write(final T entity) {
       persist(this.forCrud().cast(entity));
       return entity;
    }

    @Override
    public Collection<T> writeAll(final Collection<T> entity) {
        persist(this.castLower(entity));
        return entity;
    }

    @Override
    public void delete(final Collection<T> entities) {
        for (final C entity : this.castLower(entities)) {
            delete(entity);
        }
    }

    @Override
    public boolean exists(long id) {
        return findByIdOptional(id).isPresent();
    }

    protected abstract Class<C> forCrud();

    protected Collection<C> castLower(final Collection<T> collection) {
        final Collection<C> result;
        if (collection == null) {
            result = null;
        } else {
            result = collection.stream()
                .map(item -> this.forCrud().cast(item))
                .collect(Collectors.toList());
        }
        return result;
    }

    protected Collection<T> castUpper(final Collection<C> collection) {
        return collection.stream()
            .map(item -> this.forEntity().cast(item))
            .collect(Collectors.toList());
    }
}
