package insideworld.engine.entities.storages;

public class StorageException extends Exception {

    public StorageException(final Exception exp) {
        super(exp);
    }

    public StorageException(final String message) {
        super(message);
    }
}
