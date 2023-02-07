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

package insideworld.engine.core.endpoint.base.action.serializer;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import java.util.Arrays;
import javax.inject.Singleton;

@Singleton
public class TestArrayAction implements Action<TestInput[], TestOutput[]> {

    @Override
    public TestOutput[] execute(final TestInput[] inputs) throws CommonException {
        return Arrays.stream(inputs).map(input -> {
            final TestOutput output = new TestOutputImpl();
            output.setUUID(input.getUUID());
            return output;
        }).toArray(TestOutput[]::new);
    }

    @Override
    public String key() {
        return "insideworld.engine.core.action.serializer.TestArrayAction";
    }

    @Override
    public final void types(final TestInput[] input, final TestOutput[] output) {
        //Nothing to do.
    }
}
