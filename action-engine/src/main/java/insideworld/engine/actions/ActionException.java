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

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.tags.ActionsTags;

/**
 * Exception during to an action processing.
 *
 * @since 0.1.0
 */
public class ActionException extends Exception {

    /**
     * Default exception message.
     */
    private static final String EXCEPTION_MESSAGE = "Exception during execute action %s";

    /**
     * Constructor with message parameter.
     * @param message Exception message.
     */
    public ActionException(final String message) {
        super(message);
    }

    /**
     * Constructor to wrap an exists exception.
     * @param message Exception message.
     * @param exp Original exception.
     */
    public ActionException(final String message, final Exception exp) {
        super(message, exp);
    }

    /**
     * Contructor to wrap an exists exception with default message.
     * @param input Context with action information.
     * @param exp Orignal exception.
     */
    public ActionException(final Context input, final Exception exp) {
        super(String.format(ActionException.EXCEPTION_MESSAGE, input.get(ActionsTags.ACTION)), exp);
    }

}
