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

package insideworld.engine.entities.storages;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import java.util.Collection;

/**
 * Storage for entity.
 * Provide basic functions to work with entity.
 * @param <T> Entity type.
 * @since 0.0.1
 */
public interface Storage<T extends Entity> {

    /**
     * Read all entities.
     * @return Collection of all entities.
     * @throws StorageException Can't read.
     */
    Collection<T> readAll() throws StorageException;

    /**
     * Read entity by ID.
     * @param id ID.
     * @return Entity.
     * @throws StorageException Can't read.
     */
    T read(long id) throws StorageException;

    /**
     * Read collection of entities.
     * @param ids Ids for read.
     * @return Collection of entities.
     * @throws StorageException Can't read.
     */
    Collection<T> read(Collection<Long> ids) throws StorageException;

    /**
     * Write entity.
     * @param entity Entity.
     * @return Wrote entity.
     * @throws StorageException Can't write.
     */
    T write(T entity) throws StorageException;

    /**
     * Write all entities.
     * @param entity Entities.
     * @return Collection of wrote entities.
     * @throws StorageException Can't write.
     */
    Collection<T> writeAll(Collection<T> entity) throws StorageException;

    /**
     * Delete entities.
     * @param entities Entities.
     * @throws StorageException Can't delete.
     */
    void delete(Collection<T> entities) throws StorageException;

    /**
     * Is entity exists by this ID.
     * @param id Id.
     * @return True if exists and false if not.
     */
    boolean exists(long id);

    /**
     * Tyoe of entity for this storage.
     * @return Entity type.
     */
    Class<? extends T> forEntity();

}
