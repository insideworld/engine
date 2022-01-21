package insideworld.engine.entities.storages.keeper;

import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.reflection.Reflection;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HashStorageKeeper implements StorageKeeper {

    private final Map<Class<? extends Entity>, Storage<?>> storages;

    /**
     * Crutch for Quarkus because this stupid DI can't works with wildcards
     * @param storages
     */
    @Inject
    public HashStorageKeeper(final Collection<Storage> storages,
                             final Reflection reflection) {
        this.storages = Maps.newHashMapWithExpectedSize(storages.size() * 2);
        for (final Storage storage : storages) {
            reflection.getSubTypesOf(storage.forEntity())
                .forEach(type -> this.storages.put((Class<? extends Entity>) type, storage));
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
}
