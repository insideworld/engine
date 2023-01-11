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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import insideworld.engine.core.common.keeper.context.Context;
import insideworld.engine.core.data.core.converter.EntityConverter;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import insideworld.engine.plugins.generator.data.action.read.specific.info.SpecificReadInfo;
import insideworld.engine.plugins.generator.data.action.read.specific.search.SearchSpecificReadAction;
import insideworld.engine.plugins.generator.data.action.read.specific.search.SearchSpecificReadActionMixin;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.FieldDescriptor;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;


public class SpecificReadActionGenerator {

    private final Reflection reflection;
    private final ClassOutput output;

    public SpecificReadActionGenerator(final Reflection reflection,
                                       final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public void generate() {
        this.infos().forEach(this::generate);
    }

    private void generate(final SpecificReadInfo info) {
        ClassCreator.Builder builder = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.implementation())
            .superClass(AbstractSpecificReadAction.class)
            .signature(this.prepareSignature(info.storage().getName()));
        for (final Class<?> inter : info.interfaces()) {
            builder.interfaces(inter);
        }
        final ClassCreator creator = builder.build();
        this.createConstructor(creator, info);
        this.createMethod(creator, info);
        this.createKey(creator, info);
        creator.addAnnotation(Singleton.class);
        creator.close();
    }

    private ResultHandle executeStorage(
        final Pair<MethodCreator, Method> method,
        final SpecificReadInfo info,
        final ResultHandle storage,
        final Collection<ResultHandle> parameters
    ) {
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            info.storage(),
            info.method(),
            method.getRight().getReturnType(),
            method.getRight().getParameterTypes()
        );
        return method.getLeft().invokeInterfaceMethod(
            descriptor,
            storage,
            parameters.toArray(ResultHandle[]::new)
        );
    }

    private ResultHandle getStorage(final ClassCreator creator, final MethodCreator method) {
        final FieldDescriptor descriptor = FieldDescriptor.of(
            creator.getClassName(),
            "storage",
            Storage.class
        );
        return method.readInstanceField(descriptor, method.getThis());
    }

    private ResultHandle wrapToCollection(final MethodCreator creator, final ResultHandle handle) {
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            Collections.class,
            "singleton",
            Set.class,
            Object.class
        );
        return creator.invokeStaticMethod(
            descriptor,
            handle
        );
    }

    private void createMethod(final ClassCreator creator, final SpecificReadInfo info) {
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
            method.getLeft().returnValue(this.wrapToCollection(method.getLeft(), result));
        }
    }

    private Pair<MethodCreator, Method> createReadMethod(final ClassCreator creator, final SpecificReadInfo info) {
        final Method found = Arrays.stream(info.storage().getDeclaredMethods()).filter(
                method -> method.getName().equals(info.method())
                          && method.getParameterCount() == info.parameters().length
            )
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Not found necessary method"));
        return Pair.of(
            creator.getMethodCreator(
                "read",
                Collection.class,
                Context.class
            ),
            found
        );
    }

    private ResultHandle getParameter(final MethodCreator creator, final String key) {
        final MethodDescriptor descriptor = MethodDescriptor.ofMethod(
            Context.class,
            "get",
            Object.class,
            String.class
        );
        return creator.invokeInterfaceMethod(
            descriptor,
            creator.getMethodParam(0),
            creator.load(key)
        );
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
                EntityConverter.class,
                Storage.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0),
            constructor.getMethodParam(1)
        );
        constructor.returnValue(null);
    }

    private void createKey(final ClassCreator creator, final SpecificReadInfo info) {
        final MethodCreator method = creator.getMethodCreator("key", String.class);
        method.returnValue(method.load(info.key()));
    }

    private String prepareSignature(final String storage) {
        return String.format("L%s<L%s;>;",
            AbstractSpecificReadAction.class.getName().replace(".", "/"),
            storage.replace(".", "/")
        );
    }

    private Collection<SpecificReadInfo> infos() {
        final Collection<SearchSpecificReadAction> searchers = ImmutableList.of(
            new SearchSpecificReadActionMixin(this.reflection)
        );
        return searchers.stream().map(SearchSpecificReadAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
