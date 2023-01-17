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

package insideworld.engine.plugins.generator.data.action.read.specific;

import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;

public class GenerateMethods {



    public void createKey(final ClassCreator creator, final SpecificReadInfo info) {
        final MethodCreator method = creator.getMethodCreator("key", String.class);
        method.returnValue(method.load(info.key()));
    }

    public void createInput(final ClassCreator creator, final SpecificReadInfo info) {
        final MethodCreator method = creator.getMethodCreator("inputType", Class.class);
        method.returnValue(method.loadClass(info.getInput()));
    }

    public void createOutput(final ClassCreator creator, final Class<?> output) {
        final MethodCreator method = creator.getMethodCreator("outputType", Class.class);
        method.returnValue(method.loadClass(output));
    }

}
