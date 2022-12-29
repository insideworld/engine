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

package insideworld.engine.core.action.keeper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import insideworld.engine.core.action.keeper.tags.Tag;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * Record is storage for keep different type data using tags.
 * Only one value may be present for one tag or key in a record.
 * @see Tag
 * @since 0.1.0
 */
public interface Record extends Serializable {

    /**
     * Get value from record using string key.
     * @param key String key of row.
     * @return Value.
     * @param <T> Type of value.
     */
    <T> T get(String key);

    /**
     * Get value from record using tag.
     * @param tag Tag of value.
     * @return Value.
     * @param <T> Type of value.
     */
    <T> T get(Tag<T> tag);

    /**
     * Get optional value from record.
     * @param tag Tag of value.
     * @return Optional value.
     * @param <T> Type of optional value.
     */
    <T> Optional<T> getOptional(Tag<T> tag);

    /**
     * Check is value by key is present into record.
     * @param key String key.
     * @return True if contains, False if not.
     */
    boolean contains(String key);

    /**
     * Check is value by tag is present into record.
     * @param tag Tag.
     * @return True if contains, False if not.
     * @param <T> Type of value.
     */
    <T> boolean contains(Tag<T> tag);

    /**
     * Set a value into record using string key.
     * @param key String key.
     * @param value Value.
     * @param <T> Value type.
     * @throws IllegalArgumentException If record already contains this tag.
     */
    <T> void put(String key, T value) throws IllegalArgumentException;

    /**
     * Set a value into record using tag.
     * @param tag Tag.
     * @param value Value.
     * @param <T> Value type.
     * @throws IllegalArgumentException If record already contains this tag.
     */
    <T> void put(Tag<T> tag, T value) throws IllegalArgumentException;

    /**
     * Set a value into record using tag.
     * @param key String key.
     * @param value Value.
     * @param replace Replace record if it is already present.
     * @param <T> Value type.
     * @throws IllegalArgumentException If record already contains this tag and replace is false.
     */
    <T> void put(String key, T value, boolean replace) throws IllegalArgumentException;

    /**
     * Set a value into record using tag.
     * @param tag Tag.
     * @param value Value.
     * @param replace Replace record if it is already present.
     * @param <T> Value type.
     * @throws IllegalArgumentException If record already contains this tag and replace is false.
     */
    <T> void put(Tag<T> tag, T value, boolean replace) throws IllegalArgumentException;

    /**
     * Map representation of record.
     * @return Map with all values.
     */
    @JsonAnyGetter
    Map<String, ? super Object> values();

    /**
     * Clone tag between records.
     * @param tag Tag to copy.
     * @param record Record for clone.
     * @param <T> Type of tag.
     */
    <T> void cloneTag(Tag<T> tag, Record record);
}
