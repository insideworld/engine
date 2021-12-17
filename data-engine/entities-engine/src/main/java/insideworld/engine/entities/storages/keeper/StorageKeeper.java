package insideworld.engine.entities.storages.keeper;


import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;

public interface StorageKeeper {

    <T extends Entity> Storage<T> getStorage(Class<? extends T> type) throws StorageException;

}
