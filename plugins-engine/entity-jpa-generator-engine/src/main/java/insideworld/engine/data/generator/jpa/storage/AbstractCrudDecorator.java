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
