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
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.generator.entities.actions.abstracts.info.ActionTagInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import java.util.Collection;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;

public abstract class AbstractActionTagGenerator<T extends ActionTagInfo> {

    private ClassOutput output;

    public AbstractActionTagGenerator(final ClassOutput output) {
        this.output = output;
    }

    public void generate() {
        this.infos().forEach(this::generate);
    }

    private void generate(final T info) {
        ClassCreator.Builder builder = ClassCreator.builder()
            .classOutput(this.output)
            .className(info.implementation())
            .superClass(this.extended())
            .signature(this.prepareSignature(info.entity().getName()));
        for (final Class<?> inter : info.interfaces()) {
            builder.interfaces(inter);
        }
        final ClassCreator creator = builder.build();
        creator.addAnnotation(Singleton.class);
        this.methodPredicates().forEach(method -> method.accept(creator, info));
        creator.close();
    }

    protected Collection<BiConsumer<ClassCreator, T>> methodPredicates() {
        return ImmutableList.of(
            this::createConstructor,
            this::createTag,
            this::createKey,
            this::createType
        );
    }

    protected abstract Class<?> extended();

    protected abstract Collection<T> infos();

    private void createConstructor(final ClassCreator creator, final T info) {
        final MethodCreator constructor =
            creator.getMethodCreator("<init>", void.class, LinksBuilder.class);
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(this.extended(),
                "<init>",
                void.class,
                LinksBuilder.class
            ),
            constructor.getThis(),
            constructor.getMethodParam(0)
        );
        constructor.returnValue(null);
    }

    private void createTag(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getTag", EntityTag.class);
        final ResultHandle result = method.newInstance(
            MethodDescriptor.ofConstructor(EntityTag.class, String.class),
            method.load(info.tag())
        );
        method.returnValue(result);
    }

    private void createKey(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("key", String.class);
        method.returnValue(method.load(info.key()));
    }

    private void createType(final ClassCreator creator, final T info) {
        final MethodCreator method = creator.getMethodCreator("getType", Class.class);
        method.returnValue(method.loadClass(info.entity()));
    }

    private String prepareSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            this.extended().getName().replace(".", "/"),
            entity.replace(".", "/")
        );
    }

}
