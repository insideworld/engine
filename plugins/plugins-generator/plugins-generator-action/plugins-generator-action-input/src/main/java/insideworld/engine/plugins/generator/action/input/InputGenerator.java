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

package insideworld.engine.plugins.generator.action.input;

import insideworld.engine.plugins.generator.base.reflection.Reflection;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.ClassOutput;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import org.apache.commons.lang3.StringUtils;

public class InputGenerator {

    private Reflection reflection;
    private ClassOutput output;

    public InputGenerator(final Reflection reflection, final ClassOutput output) {
        this.reflection = reflection;
        this.output = output;
    }

    public void generate() {
        final StringBuilder errors = new StringBuilder();
        final var properties = this.find().stream().collect(Collectors.toMap(
            Function.identity(),
            type -> new Properties(type).findProperties(errors)
        ));
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.toString());
        }
        properties.forEach(this::generate);
    }

    private void generate(final Class<?> type, final Collection<Property> properties) {
        final ClassCreator creator = ClassCreator.builder()
            .classOutput(this.output)
            .interfaces(type)
            .className(String.format("%sImpl",type.getName()))
            .build();
        creator.addAnnotation(Dependent.class);
        for (final Property property : properties) {
            final FieldCreator field = creator.getFieldCreator(
                StringUtils.uncapitalize(property.getName()), property.getType()
            );
            property.getGetter().ifPresent((clazz) -> {
                final MethodCreator get = creator.getMethodCreator(
                    String.format("get%s", property.getName()),
                    property.getType()
                );
                get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
                get.close();
            });
            property.getSetter().ifPresent((clazz) -> {
                final MethodCreator set = creator.getMethodCreator(
                    String.format("set%s", property.getName()),
                    void.class,
                    property.getType()
                );
                set.writeInstanceField(
                    field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0)
                );
                set.returnValue(null);
                set.close();
            });
        }
        creator.close();
    }

    private Collection<Class<?>> find() {
        return this.reflection.getAnnotatedClasses(GenerateInput.class);
    }
}
