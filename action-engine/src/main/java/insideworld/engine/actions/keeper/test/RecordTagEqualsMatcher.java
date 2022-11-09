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

package insideworld.engine.actions.keeper.test;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Objects;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Check that record contain tag.
 * @since 0.14.0
 */
public class RecordTagEqualsMatcher extends TypeSafeMatcher<Record> {

    /**
     * Tag to equals.
     */
    private final String tag;

    /**
     * Value to equals.
     */
    private final Object value;

    /**
     * Constructor for tag object.
     * @param tag Tag object.
     * @param value Value to equals.
     */
    public RecordTagEqualsMatcher(final Tag<?> tag, final Object value) {
        this(tag.getTag(), value);
    }

    /**
     * Constructor for string key.
     * @param tag String key.
     * @param value Value to equals.
     */
    public RecordTagEqualsMatcher(final String tag, final Object value) {
        this.tag = tag;
        this.value = value;
    }

    @Override
    public final void describeTo(final Description description) {
        description
            .appendText("Expected value for tag ")
            .appendText(this.tag)
            .appendText(" is ")
            .appendValue(this.value);
    }

    @Override
    protected final boolean matchesSafely(final Record item) {
        final boolean exists;
        if (item.contains(this.tag)) {
            exists = Objects.equals(this.value, item.get(this.tag));
        } else {
            exists = false;
        }
        return exists;
    }
}
