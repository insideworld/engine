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
import javax.enterprise.context.Dependent;
import org.apache.commons.lang3.Validate;

@Dependent
public class MapRecord implements Record {

    /**
     * Collection of pair key/value.
     */
    private final Map<String, ? super Object> map;

    public MapRecord() {
        this.map = Maps.newHashMap();
    }

    /**
     * Get a value for specific type.
     * @param key String setKey.
     * @param <T> Type.
     * @return Value.
     */
    @Override
    public final <T> T get(final String key)   {
        return (T) this.map.get(key);
    }

    @Override
    public final <T> T get(final Tag<T> tag) {
        return (T) this.map.get(tag.getTag());
    }

    @Override
    public <T> Optional<T> getOptional(Tag<T> tag) {
        return Optional.ofNullable(this.get(tag));
    }

    @Override
    public final boolean contains(final String key) {
        return key != null && this.map.containsKey(key);
    }

    @Override
    public final <T> boolean contains(Tag<T> tag) {
        return tag != null && this.map.containsKey(tag.getTag());
    }


    /**
     * Get all values.
     * @return Map setKey, value.
     */
    @JsonAnyGetter
    @Override
    public final Map<String, ? super Object> values() {
        return this.map;
    }

    @Override
    public <T> Optional<T> optional(final Tag<T> tag) {
        final Optional<T> result;
        if (tag == null) {
            result = Optional.empty();
        } else {
            result = Optional.ofNullable(this.get(tag));
        }
        return result;
    }

    @Override
    public <T> void cloneTag(final Tag<T> tag, final Record to) {
        to.put(tag, this.get(tag));
    }

    /**
     * Put a value inside setKey/value set.
     * @param key String setKey.
     * @param value Inserted value.
     * @param <T> Type of inserted value.
     */
    @Override
    public final <T> void put(final String key, final T value) {
        this.put(key, value, false);
    }

    @Override
    public final <T> void put(final Tag<T> tag, final T value) {
        this.put(tag.getTag(), value, false);
    }

    @Override
    public <T> void put(Tag<T> tag, T value, boolean replace) {
        this.put(tag.getTag(), value, replace);
    }

    @Override
    public <T> void put(final String key, final T value, boolean replace) {
        Validate.isTrue(replace || !this.map.containsKey(key),
            "Record already contains this key!");
        if (value != null) {
            this.map.put(key, value);
        }
    }
}
