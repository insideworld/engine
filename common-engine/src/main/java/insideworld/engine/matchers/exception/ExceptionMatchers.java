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

package insideworld.engine.matchers.exception;

import java.lang.reflect.Executable;
import org.hamcrest.Matcher;

public final class ExceptionMatchers {

    /**
     * Private constructor.
     */
    private ExceptionMatchers() {
        //Nothing to do.
    }

    public static ExceptionClassMatcher classMatcher(
        final int level,
        final Class<? extends Throwable> exception
    ) {
        return new ExceptionClassMatcher(level,exception);
    }

    public static ExceptionMessageMatcher messageMatcher(
        final int level,
        final Matcher<String> matcher
    ) {
        return new ExceptionMessageMatcher(level, matcher);
    }

    public static ExceptionCatchMatcher catchException(final Class<? extends Throwable> throwable) {
        return new ExceptionCatchMatcher(throwable);
    }

    public static ExceptionCatchMatcher catchException(
        final Class<? extends Throwable> throwable,
        final Matcher<? extends Throwable> matcher
    ) {
        return new ExceptionCatchMatcher(throwable, matcher);
    }
}
