package insideworld.engine.data.jpa.properties;

import insideworld.engine.properties.Properties;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseProperties implements Properties {

    private final PropertyStorage storage;

    @Inject
    public DatabaseProperties(final PropertyStorage storage) {
        this.storage = storage;
    }

    @Override
    public <OUT> OUT provide(final String key, final Class<OUT> type) {
        final OUT value;
        final String property = this.storage.findById(key).getValue();
        if (type.isAssignableFrom(String.class)) {
            value = (OUT) property;
        } else {
            throw new RuntimeException("Can't convert property");
        }
        return value;
    }

    @Override
    public <OUT> boolean exists(final String key, final Class<OUT> type) {
        final Optional<JpaProperties> property = this.storage.findByIdOptional(key);
        return property.isPresent();
    }
}
