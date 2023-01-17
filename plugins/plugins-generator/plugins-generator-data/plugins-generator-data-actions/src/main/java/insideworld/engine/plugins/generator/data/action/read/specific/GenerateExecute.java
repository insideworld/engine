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

import com.google.common.collect.Lists;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.lang.reflect.Method;
import java.util.Collection;
import org.apache.commons.lang3.tuple.Pair;

public class GenerateExecute {


    private void createExecute(
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
        //It's execute storage descriptor
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            info.storage(),
            info.method(),
            storage.getReturnType(),
            storage.getParameterTypes()
        );

        final ResultHandle[] input = new ResultHandle[parameters.length];
        final ResultHandle income = execute.getMethodParam(0);
        for (int i = 0; i < parameters.length; i++) {
            execute.invokeInterfaceMethod(
                descriptor,
                this.getStorage(creator, execute),


            );
            execute.getMethodParam(0);
        }
        execute.invokeInterfaceMethod(
            descriptor,
            this.getStorage(creator, execute),

//            parameters.toArray(ResultHandle[]::new)
        );

        final Pair<MethodCreator, Method> method = this.createReadMethod(creator, info);
        final Collection<ResultHandle> parameters =
            Lists.newArrayListWithCapacity(info.parameters().length);
        for (final String parameter : info.parameters()) {
            parameters.add(this.getParameter(method.getKey(), parameter));
        }
        final ResultHandle storage = this.getStorage(creator, method.getLeft());
        final ResultHandle result = this.executeStorage(method, info, storage, parameters);
        if (Collection.class.isAssignableFrom(method.getRight().getReturnType())) {
            method.getLeft().returnValue(result);
        } else {
//            method.getLeft().returnValue(this.wrapToCollection(method.getLeft(), result));
        }
    }

    /**
     * Get storage field.
     * @param creator Class creator.
     * @param method Method for exexute.
     * @return Handler of field.
     */
    private ResultHandle getStorage(final ClassCreator creator, final MethodCreator method) {
        final FieldDescriptor descriptor = FieldDescriptor.of(
            creator.getClassName(),
            "storage",
            Storage.class
        );
        return method.readInstanceField(descriptor, method.getThis());
    }

}
