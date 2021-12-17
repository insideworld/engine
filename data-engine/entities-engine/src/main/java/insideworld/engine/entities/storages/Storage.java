package insideworld.engine.entities.storages;


import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface Storage<T extends Entity> {

    Collection<T> readAll() throws StorageException;

    T read(long id) throws StorageException;

    Collection<T> read(Collection<Long> ids) throws StorageException;

    T write(T entity);

    Collection<T> writeAll(Collection<T> entity);

    void delete(Collection<T> entities);

    boolean exists(long id);

    Class<? extends T> forEntity();
}
