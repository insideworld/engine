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

import com.google.common.primitives.Primitives;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenerateExecute {


    public void createExecute(
        final ClassCreator creator,
        final Method storage,
        final SpecificReadInfo info
    ) {
        final MethodCreator execute = creator.getMethodCreator(
            "execute",
            Object.class,
            Object.class
        );
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            info.storage(),
            info.method(),
            storage.getReturnType(),
            storage.getParameterTypes()
        );
        final ResultHandle[] parameters;
        /**
         * Fixme - move it to lambda.
         */
        if (info.getInput().isArray()
            || info.getInput().isPrimitive()
            || Primitives.isWrapperType(info.getInput())
            || Collection.class.isAssignableFrom(info.getInput())
            || String.class.equals(info.getInput())
            || Date.class.equals(info.getInput())
        ) {
            parameters = new ResultHandle[]{execute.getMethodParam(0)};
        } else {
            parameters = this.getClassParameters(info, execute, this.findMethods(storage, info));
        }
        final ResultHandle output = execute.invokeInterfaceMethod(
            descriptor,
            this.getStorageField(creator, execute),
            parameters
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
     *
     * @param info
     * @param creator
     * @param methods
     * @return
     */
    private ResultHandle[] getClassParameters(
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

    private Method[] findMethods(final Method storage, final SpecificReadInfo info) {
        final Map<String, Method> fields = Arrays.stream(info.getInput().getDeclaredMethods())
            .filter(method -> method.getName().startsWith("get"))
            .collect(Collectors.toMap(
                method -> method.getName().substring(3).toLowerCase(),
                Function.identity()
            ));
        final Method[] array = new Method[storage.getParameterCount()];
        for (int i = 0; i < info.parameters().length; i++) {
            final Method method = fields.get(info.parameters()[i].toLowerCase());
            if (method == null) {
                throw new IllegalArgumentException(
                    String.format(
                        "Can't find parameter %s for %s in input %s",
                        info.parameters()[i],
                        info.storage().getName(),
                        info.getInput().getName()
                    )
                );
            }
            array[i] = method;
        }
        return array;
    }
}
