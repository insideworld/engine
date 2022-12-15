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
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ExceptionCatchMatcher extends TypeSafeMatcher<ExecutableException> {

    private final Class<? extends Throwable> throwable;

    private final Matcher<? extends Throwable> matcher;

    private Throwable raised;

    public ExceptionCatchMatcher(final Class<? extends Throwable> throwable) {
        this(throwable, null);
    }

    public ExceptionCatchMatcher(
        final Class<? extends Throwable> throwable,
        final Matcher<? extends Throwable> matcher
    ) {
        this.throwable = throwable;
        this.matcher = matcher;
    }

    @Override
    protected boolean matchesSafely(final ExecutableException item) {
        boolean result = false;
        try {
            item.execute();
        } catch (final Throwable exp) {
            this.raised = exp;
            result = this.throwable.equals(exp.getClass())
                && (this.matcher == null || this.matcher.matches(exp));
        }
        return result;
    }

    @Override
    public void describeTo(final Description description) {
        description
            .appendText("Exception is ")
            .appendText(this.throwable.getName());
        if (this.matcher != null) {
            description.appendText(" and ");
            this.matcher.describeTo(description);
        }
    }

    @Override
    protected final void describeMismatchSafely(
        final ExecutableException item, final Description description) {
        if (this.raised == null) {
            description.appendText("Exception is not throw");
        } else {
            description.appendText("Exception is ").appendText(this.raised.getClass().getName());
        }
        if (this.matcher != null) {
            description.appendText(" and ");
            this.matcher.describeMismatch(item, description);
        }
    }
}
