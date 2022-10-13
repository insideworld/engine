/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.keeper;

import insideworld.engine.actions.keeper.tags.Tag;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

public interface Record extends Serializable {

    <T> T get(String key);

    <T> T get(Tag<T> tag);

    <T> Optional<T> getOptional(Tag<T> tag);

    boolean contains(String key);

    <T> boolean contains(Tag<T> tag);

    <T> void put(String key, T value);

    <T> void put(Tag<T> tag, T value);

    <T> void put(String key, T value, boolean replace);

    <T> void put(Tag<T> tag, T value, boolean replace);

    Map<String, ? super Object> values();

    <T> Optional<T> optional(final Tag<T> tag);

    /**
     * Clone tag between context.
     * Using for action to action execution.
     *
     * @param tag
     * @param to
     */
    <T> void cloneTag(final Tag<T> tag, final Record to);
}
