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

import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.lang.reflect.Method;

public class GenerateExecute {


    public void createExecute(
        final ClassCreator creator,
        final Method storage,
        final Method[] parameters,
        final SpecificReadInfo info
    ) {
        final MethodCreator execute = creator.getMethodCreator(
            "execute",
            storage.getReturnType(),
            info.getInput()
        );
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            info.storage(),
            info.method(),
            storage.getReturnType(),
            storage.getParameterTypes()
        );
        final ResultHandle output = execute.invokeInterfaceMethod(
            descriptor,
            this.getStorageField(creator, execute),
            this.getParameter(info, execute, parameters)
        );
        execute.returnValue(output);
    }

    /**
     * Get storage field.
     *
     * @param creator Class creator.
     * @param method Method for exexute.
     * @return Handler of field.
     */
    private ResultHandle getStorageField(final ClassCreator creator, final MethodCreator method) {
        final FieldDescriptor descriptor = FieldDescriptor.of(
            creator.getClassName(),
            "storage",
            Storage.class
        );
        return method.readInstanceField(descriptor, method.getThis());
    }

    /**
     * Get paramters result handle from input..
     * @param info
     * @param creator
     * @param methods
     * @return
     */
    private ResultHandle[] getParameter(
        final SpecificReadInfo info,
        final MethodCreator creator,
        final Method[] methods
    ) {
        final ResultHandle[] parameters = new ResultHandle[methods.length];
        final ResultHandle input = creator.getMethodParam(0);
        for (int i = 0; i < methods.length; i++) {
            final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
                info.getInput(),
                methods[i].getName(),
                methods[i].getReturnType()
            );
            parameters[i] = creator.invokeInterfaceMethod(
                descriptor,
                input
            );
        }
        return parameters;
    }
}
