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

package insideworld.engine.core.data.jpa;

import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Abstract class to implement CRUD storage types.
 * @param <T> Base entity type.
 * @param <C> CRUD entity type.
 * @since 0.0.1
 */
public abstract class AbstractCrudStorage<T extends Entity, C extends T>
    implements Storage<T>, PanacheRepository<C> {

    @Override
    public final Collection<T> readAll() {
        return this.castUpper(findAll().list());
    }

    @Override
    public final T read(final long id) throws StorageException {
        return findByIdOptional(id)
            .orElseThrow(() -> new StorageException("Can't find ", this.forEntity(), id));
    }

    @Override
    public final Collection<T> read(final Collection<Long> ids) {
        return this.castUpper(find("id in (?1)", ids).list());
    }

    @Override
    public final T write(final T entity) {
        final C crud = this.forCrud().cast(entity);
        persist(crud);
        return crud;
    }

    @Override
    public final Collection<T> writeAll(final Collection<T> entity) {
        return entity.stream().map(this::write).toList();
    }

    @Override
    public final void delete(final Collection<T> entities) {
        for (final C entity : this.castLower(entities)) {
            this.delete(entity);
        }
    }

    @Override
    public final boolean exists(final long id) {
        return findByIdOptional(id).isPresent();
    }

    /**
     * Cast entities (more common) to crud type.
     * @param collection Collection of base entities
     * @return Collection of crud entities.
     */
    protected final Collection<C> castLower(final Collection<T> collection) {
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

    /**
     * Cast CRUD's type to entities (more common).
     * @param collection CRUD entities.
     * @return Entities.
     */
    protected final Collection<T> castUpper(final Collection<C> collection) {
        return collection.stream()
            .map(item -> this.forEntity().cast(item))
            .collect(Collectors.toList());
    }

    /**
     * CRUD type of entity for this storage.
     * @return CRUD type.
     */
    protected abstract Class<C> forCrud();

    @Override
    public T merge(final T entity) {
        return this.getEntityManager().merge(entity);
    }
}
