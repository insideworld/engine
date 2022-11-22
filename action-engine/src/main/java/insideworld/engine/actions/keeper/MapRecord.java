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

package insideworld.engine.actions.keeper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.collect.Maps;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.Validate;

/**
 * Record implementation based on hash map.
 * @since 0.1.0
 */
public abstract class MapRecord implements Record {

    /**
     * Map to store keys and values.
     */
    private final Map<String, ? super Object> map;

    /**
     * Default constructor.
     */
    public MapRecord() {
        this.map = Maps.newHashMap();
    }

    @Override
    public final <T> T get(final String key)   {
        return (T) this.map.get(key);
    }

    @Override
    public final <T> T get(final Tag<T> tag) {
        final T value;
        if (tag == null) {
            value = null;
        } else {
            value = (T) this.map.get(tag.getTag());
        }
        return value;
    }

    @Override
    public final <T> Optional<T> getOptional(final Tag<T> tag) {
        final Optional<T> result;
        if (tag == null) {
            result = Optional.empty();
        } else {
            result = Optional.ofNullable(this.get(tag));
        }
        return result;
    }

    @Override
    public final boolean contains(final String key) {
        return key != null && this.map.containsKey(key);
    }

    @Override
    public final <T> boolean contains(final Tag<T> tag) {
        return tag != null && this.map.containsKey(tag.getTag());
    }

    @Override
    public final Map<String, ? super Object> values() {
        return this.map;
    }

    @Override
    public final <T> void cloneTag(final Tag<T> tag, final Record record) {
        if (tag != null) {
            record.put(tag, this.get(tag));
        }
    }

    @Override
    public final <T> void put(final String key, final T value)
        throws IllegalArgumentException {
        this.put(key, value, false);
    }

    @Override
    public final <T> void put(final Tag<T> tag, final T value)
        throws IllegalArgumentException {
        if (tag != null) {
            this.put(tag.getTag(), value, false);
        }
    }

    @Override
    public final <T> void put(final Tag<T> tag, final T value, final boolean replace)
        throws IllegalArgumentException {
        if (tag != null) {
            this.put(tag.getTag(), value, replace);
        }
    }

    @Override
    public final <T> void put(final String key, final T value, final boolean replace)
        throws IllegalArgumentException {
        Validate.isTrue(
            replace || !this.map.containsKey(key),
            "Record already contains this key!"
        );
        if (key != null && value != null) {
            this.map.put(key, value);
        }
    }
}
