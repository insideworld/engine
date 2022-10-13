/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.generator.entities.actions.write;

import com.google.common.collect.ImmutableList;
import insideworld.engine.generator.entities.actions.abstracts.AbstractActionTagGenerator;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagInfo;
import insideworld.engine.generator.entities.actions.write.search.SearchWriteAction;
import insideworld.engine.generator.entities.actions.write.search.SearchWriteMixin;
import insideworld.engine.entities.actions.AbstractWriteAction;
import insideworld.engine.generator.reflection.Reflection;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class WriteActionGenerator extends AbstractActionTagGenerator<ActionTagInfo> {

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
    protected Collection<ActionTagInfo> infos() {
        final Collection<SearchWriteAction> searchers = ImmutableList.of(
            new SearchWriteMixin(this.reflection)
        );
        return searchers.stream().map(SearchWriteAction::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
