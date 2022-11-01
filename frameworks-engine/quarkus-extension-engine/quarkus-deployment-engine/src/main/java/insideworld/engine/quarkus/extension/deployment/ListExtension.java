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

package insideworld.engine.quarkus.extension.deployment;

import io.quarkus.arc.deployment.AnnotationsTransformerBuildItem;
import io.quarkus.arc.deployment.BeanArchiveIndexBuildItem;
import io.quarkus.arc.deployment.InjectionPointTransformerBuildItem;
import io.quarkus.arc.deployment.StereotypeRegistrarBuildItem;
import io.quarkus.arc.processor.AnnotationsTransformer;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.arc.processor.InjectionPointsTransformer;
import io.quarkus.arc.processor.Transformation;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.DotName;
import org.jboss.jandex.IndexView;
import org.jboss.jandex.MethodParameterInfo;
import org.jboss.jandex.Type;

public class ListExtension {

//    @BuildStep
//    InjectionPointTransformerBuildItem addAnnotation() {
//        return new InjectionPointTransformerBuildItem(new InjectionPointsTransformer() {
//
//            public boolean appliesTo(final Type type) {
//                return type.name().equals(DotNames.LIST);
//            }
//
//            public void transform(final TransformationContext context) {
//                final AnnotationTarget target = context.getTarget();
//                final Transformation transform = context.transform();
//                final List<Type> parameters = target.asMethod().parameterTypes();
//                for (int i = 0; i < parameters.size(); i++) {
//                    Type parameter = parameters.get(i);
//                    if (parameter.name().equals(DotNames.LIST)) {
//                        transform.add(AnnotationInstance.create(
//                            DotNames.ALL,
//                            MethodParameterInfo.create(target.asMethod(), (short) i),
//                            Collections.emptyList()
//                        ));
//                    }
//                }
//                transform.done();
//            }
//        });
//    }

    @BuildStep
    AnnotationsTransformerBuildItem beanTransformer(
        final BeanArchiveIndexBuildItem beanArchiveIndexBuildItem,
        final BuildProducer<StereotypeRegistrarBuildItem> stereotypeRegistrarProducer) {
//        final IndexView index = beanArchiveIndexBuildItem.getIndex();
//        final Collection<AnnotationInstance> annotations = index.getAnnotations(DotNames.INJECT);
//        for (AnnotationInstance annotation : annotations) {
//            annotation.target().asMethod();
//        }
//        return null;
//        final Map<DotName, Set<DotName>> stereotypeScopes = getStereotypeScopes(index);
//        final Set<DotName> stereotypeAnnotations = new HashSet<>();

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
