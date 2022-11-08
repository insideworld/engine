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
import insideworld.engine.entities.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartupException;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hash map based storage keeper.
 *
 * @since 0.0.1
 */
@Singleton
public class HashStorageKeeper implements StorageKeeper, OnStartUp {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HashStorageKeeper.class);

    /**
     * Map of storages.
     * Key - entity type.
     * Value - storage for this entity type.
     */
    private final Map<Class<? extends Entity>, Storage<?>> storages;

    /**
     * Collection of all storages.
     */
    private final List<Storage<?>> all;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param all List of all storages.
     * @param factory Object factory.
     */
    @Inject
    public HashStorageKeeper(final List<Storage<?>> all, final ObjectFactory factory) {
        this.storages = Maps.newHashMapWithExpectedSize(all.size() * 2);
        this.all = all;
        this.factory = factory;
    }

    @Override
    public final <T extends Entity> Storage<T> getStorage(final Class<? extends T> type)
        throws StorageException {
        final Storage<T> storage;
        if (this.storages.containsKey(type)) {
            storage = (Storage<T>) this.storages.get(type);
        } else {
            throw new StorageException(String.format("Storage for type %s not found", type));
        }
        return storage;
    }

    @Override
    public final void startUp() throws StartupException {
        for (final Storage<?> storage : this.all) {
            try {
                this.storages.put(this.findImplementation(storage), storage);
            } catch (final StorageException exp) {
                throw new StartupException(exp);
            }
            this.storages.put(storage.forEntity(), storage);
        }
    }

    @Override
    public final int order() {
        return 5000;
    }

    /**
     * Find implementation of storage using object factory if in storage provided interface type.
     *
     * @param storage Storage.
     * @return Implementation type of entity for this storage.
     * @throws StorageException Can't find implementation.
     */
    private Class<? extends Entity> findImplementation(final Storage<?> storage)
        throws StorageException {
        HashStorageKeeper.LOGGER.trace("Init storage {} ", storage.getClass().getName());
        final Class<? extends Entity> type = this.factory.implementation(storage.forEntity());
        if (type == null) {
            throw new StorageException(
                String.format(
                    "Can't init HashStorage keeper because can't find implementation of %s for storage %s",
                    storage.forEntity().getName(),
                    storage.getClass().getName()
                )
            );
        }
        return type;
    }
}
