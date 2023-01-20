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
import java.beans.PropertyDescriptor;

public interface FieldGenerator<T extends EntityInfo> {

    void generate(ClassCreator creator, PropertyDescriptor descriptor, T info);

    boolean can(PropertyDescriptor bean, T info);

    default Class<?> propertyType(final PropertyDescriptor descriptor, final T info) {
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
}
