package insideworld.engine.entities.storages.keeper;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
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
    public HashStorageKeeper(final Collection<Storage> storages) {
        this.storages = storages.stream().collect(
            Collectors.toMap(Storage::forEntity, storage -> storage));
    }

    @Override
    public <T extends Entity> Storage<T> getStorage(final Class<? extends T> type)
        throws StorageException {
        Storage<T> storage = null;
        if (this.storages.containsKey(type)) {
            storage = (Storage<T>) this.storages.get(type);
        } else {
            for (var possible : this.storages.keySet()) {
                if (type.isAssignableFrom(possible)) {
                    storage = (Storage<T>) this.storages.get(possible);
                    break;
                }
            }
        }
        if (storage == null) {
            throw new StorageException(String.format("Storage for type %s not found", type));
        }
        return storage;
    }
}
