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

package insideworld.engine.actions;

import insideworld.engine.exception.CommonException;

/**
 * Action exception.
 * Should be thrown at action processing.
 * @since 0.0.1
 */
public class ActionException extends CommonException {

    /**
     * Default exception message.
     */
    private static final String EXCEPTION_MESSAGE =
        "Exception during process action %s with reason %s";

    /**
     * Constructor to create an exception with message.
     * @param action Action class.
     * @param message Message.
     * @param args String format arguments.
     */
    public ActionException(
        final Class<? extends Action> action, final String message, final Object... args) {
        super(
            String.format(
                ActionException.EXCEPTION_MESSAGE,
                action.getName(),
                message
            ),
            args
        );
    }

    /**
     * Constructor to create an exception with message.
     * @param action Action class.
     * @param exception Exception.
     */
    public ActionException(
        final Class<? extends Action> action, final Throwable exception) {
        super(exception, ActionException.EXCEPTION_MESSAGE, action.getName(), "Exception");
    }

    @Override
    protected final String module() {
        return "action-engine";
    }

}
