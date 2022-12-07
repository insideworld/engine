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

package insideworld.engine.exception;

import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestException {

    @Test
    public void test() {
        try {
            this.causeOne();
        } catch (OneException e) {
            final Collection<Index> indexes = e.getIndexes();
            System.out.println("qwe");
//            throw new RuntimeException(e);
        }
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