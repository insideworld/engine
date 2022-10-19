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

import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HashStorageKeeper implements StorageKeeper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashStorageKeeper.class);

    private final Map<Class<? extends Entity>, Storage<?>> storages;

    /**
     * Crutch for Quarkus because this stupid DI can't works with wildcards
     * @param storages
     */
    @Inject
    public HashStorageKeeper(final Collection<Storage> storages,
                             final ObjectFactory factory) throws StorageException {
        this.storages = Maps.newHashMapWithExpectedSize(storages.size() * 2);
        for (final Storage storage : storages) {
            this.storages.put(this.findImplementation(storage, factory), storage);
            this.storages.put(storage.forEntity(), storage);
        }
    }

    @Override
    public <T extends Entity> Storage<T> getStorage(final Class<? extends T> type)
        throws StorageException {
        final Storage<T> storage;
        if (this.storages.containsKey(type)) {
            storage = (Storage<T>) this.storages.get(type);
        } else {
            throw new StorageException(String.format("Storage for type %s not found", type));
        }
        return storage;
    }

    private Class<? extends Entity> findImplementation(
        final Storage storage, final ObjectFactory factory) throws StorageException {
        HashStorageKeeper.LOGGER.trace("Init storage " + storage.getClass().getName());
        final Object object = factory.createObject(storage.forEntity());
        if (object == null) {
            throw new StorageException(String.format(
                "Can't init HashStorage keeper because can't find implementation of %s for storage %s",
                storage.forEntity().getName(),
                storage.getClass().getName()
            ));
        }
        return (Class<? extends Entity>) object.getClass();
    }

}
