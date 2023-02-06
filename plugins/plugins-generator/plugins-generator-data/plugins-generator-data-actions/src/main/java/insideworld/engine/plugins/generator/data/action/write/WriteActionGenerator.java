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

package insideworld.engine.plugins.generator.data.action.write;

import com.google.common.collect.ImmutableList;
import insideworld.engine.core.data.core.action.AbstractWriteAction;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import insideworld.engine.plugins.generator.data.action.abstracts.AbstractActionGenerator;
import insideworld.engine.plugins.generator.data.action.abstracts.info.ActionInfo;
import insideworld.engine.plugins.generator.data.action.write.search.SearchWriteAction;
import insideworld.engine.plugins.generator.data.action.write.search.SearchWriteMixin;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import java.util.Collection;
import java.util.stream.Collectors;

public class WriteActionGenerator extends AbstractActionGenerator {

    private final Reflection reflection;

    public WriteActionGenerator(final Reflection reflection, final ClassOutput output) {
        super(output);
        this.reflection = reflection;
    }

    @Override
    protected Class<?> extended() {
        return AbstractWriteAction.class;
    }

    @Override
    protected Collection<ActionInfo> infos() {
        final Collection<SearchWriteAction> searchers = ImmutableList.of(
            new SearchWriteMixin(this.reflection)
        );
        return searchers.stream().map(SearchWriteAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Override
    protected void createTypes(final ClassCreator creator, final ActionInfo info) {
        // Input arguments is long.
        // Output argument is collection of entity.
        final MethodCreator method = creator.getMethodCreator(
            "types", void.class, Collection.class, Collection.class
        );
        final String formatted = info.entity().getName().replace(".", "/");
        method.setSignature(
            String.format(
                "(Ljava/util/Collection<L%s;>;Ljava/util/Collection<L%s;>;)V",
                formatted,
                formatted
            )
        );
        method.returnValue(null);
        method.close();
    }

}
