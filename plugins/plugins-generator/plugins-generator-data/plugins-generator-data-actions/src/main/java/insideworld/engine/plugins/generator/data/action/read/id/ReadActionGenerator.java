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

package insideworld.engine.plugins.generator.data.action.read.id;

import com.google.common.collect.ImmutableList;
import insideworld.engine.core.action.chain.LinksBuilder;
import insideworld.engine.core.data.core.action.ReadAction;
import insideworld.engine.plugins.generator.data.action.abstracts.AbstractActionGenerator;
import insideworld.engine.plugins.generator.data.action.abstracts.info.ActionInfo;
import insideworld.engine.plugins.generator.data.action.read.id.search.SearchReadAction;
import insideworld.engine.plugins.generator.data.action.read.id.search.SearchReadActionMixin;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ReadActionGenerator extends AbstractActionGenerator {

    private final Reflection reflection;

    public ReadActionGenerator(final Reflection reflection,
                               final ClassOutput output) {
        super(output);
        this.reflection = reflection;
    }

    @Override
    protected Class<?> extended() {
        return ReadAction.class;
    }

    @Override
    protected Collection<ActionInfo> infos() {
        final Collection<SearchReadAction> searchers = ImmutableList.of(
            new SearchReadActionMixin(this.reflection)
        );
        return searchers.stream().map(SearchReadAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Override
    protected final Collection<BiConsumer<ClassCreator, ActionInfo>> methodPredicates() {
        return ImmutableList.<BiConsumer<ClassCreator, ActionInfo>>builder()
            .addAll(super.methodPredicates())
            .add(this::createAfterExport)
            .build();
    }

    private void createAfterExport(final ClassCreator creator, final ActionInfo info) {
        creator
            .getMethodCreator("afterExport", void.class, LinksBuilder.class)
            .returnValue(null);
    }

}
