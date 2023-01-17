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

package insideworld.engine.frameworks.quarkus.common.extension.deployment;

import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.StereotypeRegistrarBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.arc.processor.Transformation;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import java.util.Collections;
import java.util.List;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

/**
 * Extension to inject List instead of Instance.
 * @since 1.0.0
 */
public class ListExtension {

    @BuildStep
    AnnotationsTransformerBuildItem beanTransformer(
        final BeanArchiveIndexBuildItem beanArchiveIndexBuildItem,
        final BuildProducer<StereotypeRegistrarBuildItem> stereotypeRegistrarProducer) {
        return new AnnotationsTransformerBuildItem(new AnnotationsTransformer() {

            public boolean appliesTo(org.jboss.jandex.AnnotationTarget.Kind kind) {
                return kind == AnnotationTarget.Kind.METHOD;
            }

            public void transform(TransformationContext context) {
                final AnnotationTarget target = context.getTarget();
                final Transformation transform = context.transform();
                final List<Type> parameters = target.asMethod().parameterTypes();
                for (int i = 0; i < parameters.size(); i++) {
                    Type parameter = parameters.get(i);
                    if (parameter.name().equals(DotNames.LIST)) {
                        transform.add(AnnotationInstance.create(
                            DotNames.ALL,
                            MethodParameterInfo.create(target.asMethod(), (short) i),
                            Collections.emptyList()
                        ));
                    }
                }
                transform.done();
            }
        });
    }

}
