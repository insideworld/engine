package insideworld.engine.properties;

import java.util.Collection;
import java.util.stream.Collectors;
import javax.annotation.Priority;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provide different properties from different places.
 * 
 * @since 0.0.3
 */
@Singleton
public class PropertiesProviderFacade implements PropertiesProvider {

    private final Collection<Properties> providers;

    @Inject
    public PropertiesProviderFacade(final Collection<Properties> providers) {
        this.providers = providers.stream()
            .filter(provider -> !provider.getClass().equals(this.getClass()))
            .collect(Collectors.toList());
    }
    
    @Override
    public <OUT> OUT provide(final String key, Class<OUT> type) throws PropertiesException {
        //TODO: Make cachable.
        for (final Properties provider : this.providers) {
            if (provider.exists(key, type)) {
                return provider.provide(key, type);
            }
        }
        throw new IllegalArgumentException(String.format("Key %s is not exists.", key));
    }

    @Override
    public <OUT> boolean exists(final String key, final Class<OUT> type) {
        return this.providers.stream().anyMatch(provider -> provider.exists(key, type));
    }
}
