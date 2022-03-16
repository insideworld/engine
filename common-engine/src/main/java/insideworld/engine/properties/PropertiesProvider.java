package insideworld.engine.properties;

import java.util.Optional;

/**
 * Provide a properties by string key.
 */
public interface PropertiesProvider {
    
    <OUT> OUT provide(String key, Class<OUT> type);

    <OUT> boolean exists(String key, Class<OUT> type);
    
}
