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

package insideworld.engine.plugins.generator.data.action.abstracts;

import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.plugins.generator.data.action.abstracts.info.ActionInfo;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

public abstract class AbstractActionGenerator {

    private ClassOutput output;

    public AbstractActionGenerator(final ClassOutput output) {
        this.output = output;
    }

    public void generate() {
        this.infos().forEach(this::generate);
    }

    private void generate(final ActionInfo info) {
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
        this.createConstructor(creator, info);
        this.createTypes(creator, info);
        creator.close();
    }

    protected abstract Class<?> extended();

    protected abstract Collection<ActionInfo> infos();

    /**
     * Create type method.
     * @param creator
     * @param info
     */
    protected abstract void createTypes(final ClassCreator creator, final ActionInfo info);

    private void createConstructor(final ClassCreator creator, final ActionInfo info) {
        final MethodCreator constructor = creator.getMethodCreator(
            "<init>",
            void.class,
            Storage.class
        );
        constructor.addAnnotation(Inject.class);
        constructor.invokeSpecialMethod(
            MethodDescriptor.ofMethod(
                this.extended(),
                "<init>",
                void.class,
                String.class,
                Storage.class
            ),
            constructor.getThis(),
            constructor.load(info.key()),
            constructor.getMethodParam(0)
        );
        constructor.setSignature(
            String.format(
                "(Linsideworld/engine/core/data/core/storages/Storage<L%s;>;)V",
                info.entity().getName().replace(".", "/")
            )
        );
        constructor.returnValue(null);
    }

    private String prepareSignature(final String entity) {
        return String.format("L%s<L%s;>;",
            this.extended().getName().replace(".", "/"),
            entity.replace(".", "/")
        );
    }

}
