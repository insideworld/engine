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

package insideworld.engine.core.common.matchers.exception;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ExceptionClassMatcher extends TypeSafeMatcher<Throwable> {

    private final int level;
    private final Class<? extends Throwable> throwable;

    public ExceptionClassMatcher(final int level, final Class<? extends Throwable> throwable) {
        this.level = level;
        this.throwable = throwable;
    }

    @Override
    protected boolean matchesSafely(final Throwable item) {
        Throwable toequals = item;
        for (int i = 0; i < this.level; i++) {
            toequals = toequals.getCause();
        }
        return this.throwable.equals(toequals.getClass());
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText(
            String.format(
                "Exception at level %d is %s ",
                this.level,
                this.throwable.getName()
            )
        );
    }

    @Override
    protected final void describeMismatchSafely(
        final Throwable item, final Description description) {
        description.appendText(
            String.format(
                "Exception at level %d is %s ",
                this.level,
                item.getClass().getName()
            )
        );
    }
}
