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

package insideworld.engine.core.action.keeper.test;

import insideworld.engine.core.action.keeper.Record;
import insideworld.engine.core.action.keeper.tags.Tag;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Check that record contain tag.
 * @since 0.14.0
 */
public class RecordMatcher extends TypeSafeMatcher<Record> {

    /**
     * Tag to equals.
     */
    private final String tag;

    /**
     * Matcher.
     */
    private final Matcher<?> matcher;

    /**
     * Constructor for tag object.
     * @param tag Tag object.
     * @param matcher Matcher.
     */
    public RecordMatcher(final Tag<?> tag, final Matcher<?> matcher) {
        this(tag.getTag(), matcher);
    }

    /**
     * Constructor for string key.
     * @param tag String key.
     * @param matcher Value to equals.
     */
    public RecordMatcher(final String tag, final Matcher<?> matcher) {
        this.tag = tag;
        this.matcher = matcher;
    }

    @Override
    public final void describeTo(final Description description) {
        description
            .appendText("Wrong expected value for ")
            .appendText(this.tag);
    }

    @Override
    protected final boolean matchesSafely(final Record item) {
        return this.matcher.matches(item.get(this.tag));
    }
}
