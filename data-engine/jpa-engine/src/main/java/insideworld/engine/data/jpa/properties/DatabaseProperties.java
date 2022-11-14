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

package insideworld.engine.data.jpa.properties;

import insideworld.engine.properties.Properties;
import insideworld.engine.properties.PropertiesException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Implementation of properties which keep fields in database.
 * @since 0.0.1
 */
@Singleton
public class DatabaseProperties implements Properties {

    /**
     * Storage.
     */
    private final PropertyStorage storage;

    /**
     * Default constructor.
     * @param storage Property storage.
     */
    @Inject
    public DatabaseProperties(final PropertyStorage storage) {
        this.storage = storage;
    }

    @Override
    public final <O> O provide(final String key, final Class<O> type) throws PropertiesException {
        final O value;
        final String property = this.storage.findById(key).getValue();
        if (type.isAssignableFrom(String.class)) {
            value = (O) property;
        } else {
            throw new PropertiesException("Can't convert property");
        }
        return value;
    }

    @Override
    public final <O> boolean exists(final String key, final Class<O> type) {
        final Optional<PropertyJpaEntity> property = this.storage.findByIdOptional(key);
        return property.isPresent();
    }
}
