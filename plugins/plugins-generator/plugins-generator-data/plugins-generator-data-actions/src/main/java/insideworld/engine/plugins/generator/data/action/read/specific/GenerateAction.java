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

import insideworld.engine.core.data.core.converter.EntityConverter;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.lang.reflect.Method;
import javax.inject.Inject;
import javax.inject.Singleton;

public class GenerateAction {

    private final Reflection reflection;
    private final ClassOutput output;

    public GenerateAction(final Reflection reflection,
                          final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public ClassCreator createClass(
        final SpecificReadInfo info,
        final Method storage
    ) {
        ClassCreator.Builder builder = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.implementation())
            .superClass(AbstractSpecificReadAction.class)
            .signature(this.prepareSignature(
                    storage.getReturnType().getName(),
                    info.getInput().getName(),
                    info.storage().getName()
                )
            );
        for (final Class<?> inter : info.interfaces()) {
            builder.interfaces(inter);
        }
        final ClassCreator creator = builder.build();
        this.createConstructor(creator, info);
        creator.addAnnotation(Singleton.class);
        return creator;
    }

    private void createConstructor(final ClassCreator creator, final SpecificReadInfo info) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>",
                void.class,
                EntityConverter.class,
                info.storage()
            );
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(AbstractSpecificReadAction.class,
                "<init>",
                void.class,
                Storage.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
        constructor.returnValue(null);
    }

    private String prepareSignature(
        final String input,
        final String output,
        final String storage
    ) {
        return String.format("L%s<L%s;L%s;L%s;>;",
            AbstractSpecificReadAction.class.getName().replace(".", "/"),
            input.replace(".", "/"),
            output.replace(".", "/"),
            storage.replace(".", "/")
        );
    }

}
