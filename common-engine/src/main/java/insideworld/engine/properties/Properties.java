package insideworld.engine.properties;

/**
 * Stupid CDI can't work with multiple beans because it's a full copy of Properties Provider
 */
public interface Properties {

    <OUT> OUT provide(String key, Class<OUT> type) throws PropertiesException;

    <OUT> boolean exists(String key, Class<OUT> type);

}
