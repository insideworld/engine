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

package insideworld.engine.plugins.generator.data.inmemory.entity;

import com.google.common.collect.ImmutableList;
import insideworld.engine.plugins.generator.data.inmemory.entity.search.InMemoryInfo;
import insideworld.engine.plugins.generator.data.inmemory.entity.search.SearchEntities;
import insideworld.engine.plugins.generator.data.inmemory.entity.search.SearchMixin;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.stream.Collectors;

public class InMemoryEntityGenerator {

    private final ClassOutput output;
    private final Reflection reflection;

    public InMemoryEntityGenerator(final ClassOutput output,
                                   final Reflection reflection) {
        this.output = output;
        this.reflection = reflection;
    }

    public void generate() {
        final InMemoryEntityClassGenerator entity = new InMemoryEntityClassGenerator(this.output);
        final InMemoryEntityFieldsGenerator fields = new InMemoryEntityFieldsGenerator();
        for (final InMemoryInfo info : this.findInfos()) {
            try {
                final ClassCreator creator = entity.createEntity(info);
                fields.createFields(creator, info);
                creator.close();
            } catch (final Exception exp) {
                throw new RuntimeException(
                    "Can't create entity for type" + info.getEntity().getName(),
                    exp
                );
            }
        }
    }

    private Collection<InMemoryInfo> findInfos() {
        final Collection<SearchEntities> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchEntities::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

}
