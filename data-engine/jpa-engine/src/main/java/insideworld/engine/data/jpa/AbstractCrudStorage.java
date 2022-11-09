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

package insideworld.engine.data.jpa;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
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
        final C crud = this.forCrud().cast(entity);
        final C merged;
        if (isPersistent(crud)) {
            merged = crud;
        } else {
            merged = getEntityManager().merge(crud);
        }
        persist(merged);
        return merged;
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
