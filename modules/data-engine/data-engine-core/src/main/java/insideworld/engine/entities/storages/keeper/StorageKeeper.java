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

package insideworld.engine.entities.storages.keeper;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;

/**
 * Storage keeper.
 * Can provide necessary storage for entity type.
 * @since 0.0.1
 */
public interface StorageKeeper {

    /**
     * Get storage by entity type.
     * @param type Entity type.
     * @return Storage for this type of entity.
     * @param <T> Entity type.
     * @throws StorageException Can't find storage for this entity.
     */
    <T extends Entity> Storage<T> getStorage(Class<? extends T> type) throws StorageException;

}
