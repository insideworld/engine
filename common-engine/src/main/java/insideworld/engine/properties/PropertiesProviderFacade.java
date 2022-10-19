/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.properties;

import java.util.Collection;
import java.util.Optional;
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
    public <OUT> OUT provide(final String key, Class<OUT> type) {
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
