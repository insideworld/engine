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

import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import io.quarkus.test.junit.QuarkusTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestException {

    @Test
    public void test() throws CommonException {
        MatcherAssert.assertThat(
            "Exception is not wrapped",
            () -> {
                try {
                    this.causeOne();
                } catch (final Exception exp) {
                    throw CommonException.wrap(exp, () -> new TwoException(exp), TwoException.class);
                }
            },
            ExceptionMatchers.catchException(
                TwoException.class,
                Matchers.allOf(
                    ExceptionMatchers.classMatcher(1, OneException.class),
                    ExceptionMatchers.classMatcher(2, TwoException.class),
                    ExceptionMatchers.classMatcher(3, IllegalArgumentException.class)
                )
            )
        );
        MatcherAssert.assertThat(
            "Exception is wrapped",
            () -> {
                try {
                    this.causeTwo();
                } catch (final Exception exp) {
                    throw CommonException.wrap(exp, () -> new TwoException(exp), TwoException.class);
                }
            },
            ExceptionMatchers.catchException(
                TwoException.class,
                ExceptionMatchers.classMatcher(1, IllegalArgumentException.class)
            )
        );
    }

    private void causeOne() throws OneException {
        try {
            this.causeTwo();
        } catch (TwoException exp) {
            throw new OneException(exp);
        }
    }

    private void causeTwo() throws TwoException {
        throw new TwoException(new IllegalArgumentException("Some"));
    }

}
