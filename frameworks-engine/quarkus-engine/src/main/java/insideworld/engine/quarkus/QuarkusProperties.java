package insideworld.engine.quarkus;


import insideworld.engine.properties.Properties;
import insideworld.engine.properties.PropertiesException;
import javax.inject.Singleton;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

@Singleton
public class QuarkusProperties implements Properties {

    private final Config config;

    public QuarkusProperties() {
        this.config = ConfigProvider.getConfig();
    }

    @Override
    public <OUT> OUT provide(final String key, final Class<OUT> type) {
        return this.config.getValue(key, type);
    }

    @Override
    public <OUT> boolean exists(final String key, final Class<OUT> type) {
        return this.config.getValue(key, type) != null;
    }
}
