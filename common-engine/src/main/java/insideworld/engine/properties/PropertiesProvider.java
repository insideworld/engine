package insideworld.engine.properties;

/**
 * Provide a properties by string key.
 */
public interface PropertiesProvider {
    
    <OUT> OUT provide(String key, Class<OUT> type) throws PropertiesException;

    <OUT> boolean exists(String key, Class<OUT> type);
    
}
