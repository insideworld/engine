/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
