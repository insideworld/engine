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
import com.google.common.collect.Maps;
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
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
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
        final Pair<Method, Map<Integer, Method>> pair = this.findMethod(info);
        final Method method = pair.getLeft();
        final Map<Integer, Method> parameters = pair.getRight();
        ClassCreator.Builder builder = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.implementation())
            .superClass(AbstractSpecificReadAction.class)
            .signature(this.prepareSignature(
                    method.getReturnType().getName(),
                    info.getInput().getName(),
                    info.storage().getName()
                )
            );
        for (final Class<?> inter : info.interfaces()) {
            builder.interfaces(inter);
        }
        final ClassCreator creator = builder.build();
        this.createConstructor(creator, info);
        this.createExecute(creator, info);
        this.createKey(creator, info);
        this.createInput(creator, info);
        this.createOutput(creator, method.getReturnType());
        creator.addAnnotation(Singleton.class);
        creator.close();
    }

    /**
     * Find available method in storage to execute.
     *
     * @param info Info.
     * @return Pair with storage methods and map of position and input method reference.
     */
    private Pair<Method, Method[]> findMethod(final SpecificReadInfo info) {
        final Method found = Arrays.stream(info.storage().getDeclaredMethods()).filter(
                method -> method.getName().equals(info.method())
            )
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Not found necessary method"));
        final Map<String, Method> fields = Arrays.stream(info.getInput().getDeclaredMethods())
            .filter(method -> method.getName().startsWith("get"))
            .collect(Collectors.toMap(
                method -> method.getName().substring(3),
                Function.identity()
            ));
        final Method[] array = new Method[found.getParameterCount()];
        for (int i = 0; i < found.getParameterCount(); i++) {
            final Parameter parameter = found.getParameters()[i];
            final Method method = fields.get(parameter.getName());
            if (method == null) {
                throw new IllegalArgumentException(
                    String.format(
                        "Can't find parameter %s for %s in input %s",
                        parameter.getName(),
                        info.storage().getName(),
                        info.getInput().getName()
                    )
                );
            }
            array[i] = method;
        }
        return Pair.of(found, array);
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







    private Collection<SpecificReadInfo> infos() {
        final Collection<SearchSpecificReadAction> searchers = ImmutableList.of(
            new SearchSpecificReadActionMixin(this.reflection)
        );
        return searchers.stream().map(SearchSpecificReadAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
