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

package insideworld.engine.plugins.generator.data.inmemory.storage;

import com.google.common.collect.ImmutableList;
import insideworld.engine.plugins.generator.data.base.StorageInfo;
import insideworld.engine.plugins.generator.data.inmemory.storage.abstracts.AbstractMemoryStorage;
import insideworld.engine.plugins.generator.data.inmemory.storage.search.SearchMixin;
import insideworld.engine.plugins.generator.data.inmemory.storage.search.SearchStorages;
import insideworld.engine.plugins.generator.base.reflection.Reflection;
import insideworld.engine.core.common.injection.ObjectFactory;
import io.quarkus.arc.DefaultBean;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

public class InMemoryStorageGenerator {

    private final ClassOutput output;
    private final Reflection reflection;

    public InMemoryStorageGenerator(final ClassOutput output,
                                    final Reflection reflection) {
        this.output = output;
        this.reflection = reflection;
    }

    public void generate() {
        for (final StorageInfo info : this.findStorages()) {
            final String name = info.getImplementation();
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .className(name)
                .superClass(AbstractMemoryStorage.class)
                .signature(this.prepareStorageSignature(info.getEntity().getName()))
                .build();
            this.createConstructor(creator);
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

    private String prepareStorageSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            AbstractMemoryStorage.class.getName().replace(".", "/"),
            entity.replace(".", "/")
            );
    }

    private void createConstructor(final ClassCreator creator) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>", void.class, ObjectFactory.class);
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(AbstractMemoryStorage.class,
                "<init>",
                void.class,
                ObjectFactory.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
        constructor.returnValue(null);
    }

}
