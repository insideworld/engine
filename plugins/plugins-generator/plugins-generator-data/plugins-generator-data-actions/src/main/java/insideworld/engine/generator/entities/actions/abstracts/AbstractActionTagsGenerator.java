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

package insideworld.engine.generator.entities.actions.abstracts;

import com.google.common.collect.ImmutableList;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagsInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.function.BiConsumer;

public abstract class AbstractActionTagsGenerator<T extends ActionTagsInfo>
    extends AbstractActionTagGenerator<T> {

    public AbstractActionTagsGenerator(final ClassOutput output) {
        super(output);
    }

    @Override
    protected Collection<BiConsumer<ClassCreator, T>> methodPredicates() {
        return ImmutableList.<BiConsumer<ClassCreator, T>>builder()
            .addAll(super.methodPredicates())
            .add(this::createTags)
            .build();
    }

    private void createTags(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getTags", EntitiesTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntitiesTag.class, String.class),
            method.load(info.tags())
        );
        method.returnValue(result);
    }

}
