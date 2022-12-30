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

package insideworld.engine.core.action.chain;

import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.exception.Diagnostic;
import java.util.Collection;
import java.util.Collections;

/**
 * Exception during link execute.
 * @since 0.14.0
 */
public class LinkException extends CommonException {

    /**
     * Process with link instance.
     * @param link Link type.
     * @param exception Exception.
     */
    public LinkException(final Link link, final Throwable exception) {
        super(
            LinkException.diagnostic(link),
            exception
        );
    }

    /**
     * Process with link instance.
     * @param link Link instance.
     * @param message Message with string format.
     * @param args String format args.
     */
    public LinkException(final Link link, final String message, final Object... args) {
        super(
            LinkException.diagnostic(link),
            message,
            args
        );
    }

    /**
     * Get diagnostic information about failed link.
     * @param link Link.
     * @return Diagnostic info.
     */
    private static Collection<Diagnostic> diagnostic(final Link link) {
        return Collections.singleton(new Diagnostic("link", link.getClass().getName()));
    }
}
