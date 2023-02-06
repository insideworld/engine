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

    private final GenerateAction action;

    public SpecificReadActionGenerator(final Reflection reflection,
                                       final ClassOutput output) {
        this.reflection = reflection;
        this.action = new GenerateAction(reflection, output);
    }

    public void generate() {
        this.infos().forEach(this::generate);
    }


    private void generate(final SpecificReadInfo info) {
        this.action.createClass(info, this.findMethod(info));
    }

    /**
     * Find available method in storage to execute.
     *
     * @param info Info.
     * @return Pair with storage methods and map of position and input method reference.
     */
    private Method findMethod(final SpecificReadInfo info) {
        return Arrays.stream(info.storage().getDeclaredMethods()).filter(
                method -> info.method().equals(method.getName())
            )
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Not found necessary method"));

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
