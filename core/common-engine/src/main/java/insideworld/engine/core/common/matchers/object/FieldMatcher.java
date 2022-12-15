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

package insideworld.engine.core.common.matchers.object;

import java.lang.reflect.Field;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class FieldMatcher<T> extends TypeSafeMatcher<T> {

    private final String field;
    private final Matcher<?> matcher;

    public FieldMatcher(final String field, final Matcher<?> matcher) {
        this.field = field;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(T item) {
        final Class<?> type = item.getClass();
        boolean result;
        try {
            final Field meta = type.getDeclaredField(this.field);
            final boolean access = meta.canAccess(item);
            if (!access) {
                meta.setAccessible(true);
            }
            final Object object = meta.get(item);
            meta.setAccessible(access);
            result = this.matcher.matches(object);
        }  catch (final NoSuchFieldException | IllegalAccessException exp) {
            result = false;
        }
        return result;
    }

    @Override
    public void describeTo(Description description) {

    }
}
