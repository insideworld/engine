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

package insideworld.engine.core.common.exception;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Common exception for all exceptions in engine.
 *
 * @since 0.14.0
 */
public abstract class CommonException extends Exception {

    /**
     * Diagnostic information collection.
     */
    private final Collection<Diagnostic> diagnostics;

    /**
     * Create exception based on another exception.
     *
     * @param exception Exception.
     */
    public CommonException(final Collection<Diagnostic> diagnostics, final Throwable exception) {
        super(exception);
        this.diagnostics = diagnostics;
    }

    /**
     * Create exception based on string format message.
     *
     * @param message String format message.
     * @param args String format args.
     */
    public CommonException(
        Collection<Diagnostic> diagnostics,
        final String message,
        final Object... args
    ) {
        super(String.format(message, args));
        this.diagnostics = ImmutableList.<Diagnostic>builder()
            .add(new Diagnostic("message", this.getMessage()))
            .addAll(diagnostics).build();
    }

    /**
     * Create exception based on another exception with string format message.
     *
     * @param exception Exception.
     * @param message String format message.
     * @param args String format args.
     */
    public CommonException(
        Collection<Diagnostic> diagnostics,
        final Throwable exception,
        final String message,
        final Object... args
    ) {
        super(String.format(message, args), exception);
        this.diagnostics = ImmutableList.<Diagnostic>builder()
            .add(new Diagnostic("message", this.getMessage()))
            .addAll(diagnostics).build();
    }

    /**
     * Wrap exception to necessary. In case if exception was raised with the same type - return it.
     * @param exception Raised exception.
     * @param predicate Predicate to create a new exception.
     * @param clazz Class of necessary exception.
     * @return If raised exception is assignamble to provided - return the same.
     *  Else execute predicate to wrap.
     * @param <T> Type of exception.
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static <T extends CommonException> T wrap(
        final Exception exception,
        final Supplier<T> predicate,
        final Class<T> clazz) {
        if (exception.getClass().isAssignableFrom(clazz)) {
            return (T) exception;
        } else {
            return predicate.get();
        }
    }

    public final Collection<Index> getIndexes() {
        final List<Throwable> throwables = ExceptionUtils.getThrowableList(this);
        final Collection<Index> results = Lists.newLinkedList();
        for (int idx = 0; idx < throwables.size(); idx++) {
            final Throwable throwable = throwables.get(idx);
            if (!CommonException.class.isAssignableFrom(throwable.getClass())) {
                results.add(
                    new Index(idx, new Diagnostic("unhandled", throwable.getClass().getName()))
                );
                results.add(new Index(idx, new Diagnostic("message", throwable.getMessage())));
                break;
            }
            final CommonException common = (CommonException) throwable;
            final Collection<Diagnostic> diagnostics = common.getDiagnostics();
            if (CollectionUtils.isEmpty(diagnostics)) {
                continue;
            }
            for (final Diagnostic diagnostic : diagnostics) {
                results.add(new Index(idx, diagnostic));
            }
        }
        return results;
    }

    @Override
    public final String toString() {
        return getLocalizedMessage();
    }

    private Collection<Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }


}
