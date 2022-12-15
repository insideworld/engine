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

package insideworld.engine.plugins.generator.data.jpa.storage;

import com.google.common.collect.ImmutableList;
import insideworld.engine.plugins.generator.data.base.StorageInfo;
import insideworld.engine.plugins.generator.data.jpa.entity.search.JpaInfo;
import insideworld.engine.plugins.generator.data.jpa.storage.search.SearchMixin;
import insideworld.engine.plugins.generator.data.jpa.storage.search.SearchStorages;
import insideworld.engine.core.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import io.quarkus.arc.DefaultBean;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Singleton;

public class StorageGenerator {

    private final ClassOutput output;
    private final Reflection reflection;
    private final Map<Class<? extends Entity>, JpaInfo> impls;

    public StorageGenerator(final ClassOutput output,
                            final Reflection reflection,
                            final Map<Class<? extends Entity>, JpaInfo> impls) {
        this.output = output;
        this.reflection = reflection;
        this.impls = impls;
    }

    /**
     * Default bean не ищуться CDI поэтому если мы хотим перегрузить наш сторедж то надо
     *
     */
    public void generate() {
        for (final StorageInfo info : this.findStorages()) {
            final String name = info.getImplementation();
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .className(name)
                .superClass(AbstractCrudGenericStorage.class)
                .signature(
                    this.prepareStorageSignature(
                        info.getEntity().getName(),
                        this.impls.get(info.getEntity()).getImplementation()
                    ))
                .build();
            creator.addAnnotation(Singleton.class);
            if (info.isOverride()) {
                creator.addAnnotation(DefaultBean.class);
            }
            creator.close();
        }
    }

    private Collection<StorageInfo> findStorages() {
        final Collection<SearchStorages> searchers = ImmutableList.of(
            new SearchMixin(this.reflection)
        );
        return searchers.stream().map(SearchStorages::search)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private String prepareStorageSignature(final String entity, final String jpa) {
        return String.format("L%s<L%s;L%s;>;",
            AbstractCrudGenericStorage.class.getName().replace(".", "/"),
            entity.replace(".", "/"),
            jpa.replace(".", "/")
            );
    }

}
