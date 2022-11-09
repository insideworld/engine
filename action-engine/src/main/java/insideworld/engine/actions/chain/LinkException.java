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

package insideworld.engine.actions.chain;

import insideworld.engine.exception.CommonException;

/**
 * Exception during link execute.
 * @since 0.14.0
 */
public class LinkException extends CommonException {

    /**
     * Process with link instance.
     * @param exception Exception.
     * @param link Link type.
     */
    public LinkException(final Throwable exception, final Class<? extends Link> link) {
        super(exception, "Exception in link %s", link.getName());
    }

    /**
     * Process with link instance.
     * @param link Exception.
     * @param message Message with string format.
     * @param args String format args.
     */
    public LinkException(
        final Class<? extends Link> link, final String message, final Object... args) {
        super(String.format("Exception in link %s with reason %s", link.getName(), message), args);
    }

    @Override
    protected final String module() {
        return "action-engine-chain";
    }
}
