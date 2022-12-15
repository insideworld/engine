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

package insideworld.engine.plugins.generator.data.base;

import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.SignatureElement;
import java.beans.PropertyDescriptor;

public abstract class AbstractFieldGenerator<T extends EntityInfo> implements FieldGenerator<T> {

    @Override
    public void generate(final ClassCreator creator,
                         final PropertyDescriptor descriptor,
                         final T info) {
        final FieldCreator field = creator.getFieldCreator(
            descriptor.getName(),
            this.defineType(descriptor, info)
        );
        if (field instanceof SignatureElement) {
            ((SignatureElement<?>) field).setSignature(this.fieldSignature(descriptor));
        }
        this.addAnnotations(field, descriptor, info);
        if (descriptor.getReadMethod() != null) {
            final MethodCreator get = creator.getMethodCreator(
                descriptor.getReadMethod().getName(),
                descriptor.getReadMethod().getReturnType()
            );
            get.setSignature(this.readSignature(descriptor));
            get.returnValue(get.readInstanceField(field.getFieldDescriptor(), get.getThis()));
            get.close();
        }
        if (descriptor.getWriteMethod() != null) {
            final MethodCreator set = creator.getMethodCreator(
                descriptor.getWriteMethod().getName(),
                void.class,
                descriptor.getWriteMethod().getParameterTypes()[0]
            );
            set.setSignature(this.writeSignature(descriptor));
            set.writeInstanceField(field.getFieldDescriptor(), set.getThis(), set.getMethodParam(0));
            set.returnValue(null);
            set.close();
        }
    }

    protected Class<?> propertyType(final PropertyDescriptor descriptor, final T info) {
        final Class<?> type;
        if (descriptor.getReadMethod() != null) {
            type = descriptor.getReadMethod().getReturnType();
        } else if (descriptor.getWriteMethod() != null) {
            type = descriptor.getWriteMethod().getParameterTypes()[0];
        } else {
            throw new RuntimeException(
                String.format("Can't define type of field %s for class %s because getter or setter is absent",
                    descriptor.getName(), info.getEntity().getName()));
        }
        return type;
    }

    protected abstract void addAnnotations(FieldCreator field, PropertyDescriptor descriptor, T info);

    protected abstract String defineType(PropertyDescriptor descriptor, T info);

    protected abstract String fieldSignature(PropertyDescriptor descriptor);

    protected abstract String readSignature(PropertyDescriptor descriptor);

    protected abstract String writeSignature(PropertyDescriptor descriptor);

}
