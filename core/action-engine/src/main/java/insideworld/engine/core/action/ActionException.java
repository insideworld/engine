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

package insideworld.engine.core.action;

import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.exception.Diagnostic;
import java.util.Collections;

/**
 * Action exception.
 * Should be thrown at action processing.
 *
 * @since 0.0.1
 */
@SuppressWarnings("PMD.OnlyOneConstructorShouldDoInitialization")
public class ActionException extends CommonException {

    /**
     * Constructor to create an exception with message.
     *
     * @param action Action instance.
     * @param message Message.
     * @param args String format arguments.
     */
    public ActionException(final Action<?,?> action, final String message, final Object... args) {
        super(
            Collections.singleton(new Diagnostic("action", action.key())),
            message,
            args
        );
    }

    /**
     * Constructor to create an exception with message.
     *
     * @param action Action instance.
     * @param exception Exception.
     */
    public ActionException(final Action<?,?> action, final Throwable exception) {
        super(
            Collections.singleton(new Diagnostic("action", action.key())),
            exception
        );
    }

}
