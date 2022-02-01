package insideworld.engine.generator.links;

import insideworld.engine.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;

public class LinkInputGenerator {

    private Reflection reflection;
    private ClassOutput output;

    public LinkInputGenerator(final Reflection reflection, final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public void generate() {
        for (final Class<? extends LinkInput> input : find()) {
            final ClassCreator creator = ClassCreator.builder()
                .classOutput(this.output)
                .superClass(AbstractLinkInput.class)
                .className(input.getName() + "Generated")
                .interfaces(input)
                .build();

            creator.addAnnotation(Dependent.class);
            final PropertyDescriptor[] beans;
            try {
                beans = Introspector.getBeanInfo(input).getPropertyDescriptors();
            } catch (final IntrospectionException exp) {
                throw new RuntimeException(exp);
            }
            for (final PropertyDescriptor bean : beans) {
                if (bean.getName().equals("context")) {
                    continue;
                }
                if (bean.getReadMethod() != null) {
                    final MethodCreator method = creator.getMethodCreator(
                        bean.getReadMethod().getName(), bean.getReadMethod().getReturnType());
                    final MethodDescriptor read = MethodDescriptor.ofMethod(
                        AbstractLinkInput.class, "readContext", Object.class, String.class
                    );
                    method.returnValue(method.invokeSpecialMethod(
                        read, method.getThis(), method.load(bean.getName())));
                }
                if (bean.getWriteMethod() != null) {
                    final MethodCreator method = creator.getMethodCreator(
                        bean.getWriteMethod().getName(),
                        void.class,
                        bean.getWriteMethod().getParameterTypes()[0]
                    );
                    final MethodDescriptor write = MethodDescriptor.ofMethod(
                        AbstractLinkInput.class, "writeContext",
                        void.class, String.class,
                        Object.class
                    );
                    method.invokeSpecialMethod(
                        write,
                        method.getThis(),
                        method.load(bean.getName()),
                        method.getMethodParam(0)
                        );
                    method.returnValue(null);
                }
            }
            creator.close();
        }


    }

//    private FieldCreator createContext(
//        final PropertyDescriptor[] beans, final ClassCreator creator) {
//        final PropertyDescriptor context =
//            Arrays.stream(beans)
//                .filter(bean -> bean.getName().equals("context"))
//                .findAny()
//                .orElseThrow();
//        final FieldCreator field = creator.getFieldCreator(
//            context.getName(),
//            Context.class
//        );
//        final MethodCreator method = creator.getMethodCreator(
//            context.getWriteMethod().getName(),
//            void.class,
//            Context.class
//        );
//        method.writeInstanceField(
//            field.getFieldDescriptor(),
//            method.getThis(),
//            method.getMethodParam(0)
//        );
//        method.returnValue(null);
//        return field;
//    }

    private Collection<Class<? extends LinkInput>> find() {
        return this.reflection.getSubTypesOf(LinkInput.class).stream()
            .filter(Class::isInterface)
            .collect(Collectors.toList());
    }


}
