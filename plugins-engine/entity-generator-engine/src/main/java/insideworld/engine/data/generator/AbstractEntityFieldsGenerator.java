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

package insideworld.engine.data.generator;

import io.quarkus.gizmo.ClassCreator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;

public abstract class AbstractEntityFieldsGenerator<T extends EntityInfo> {

    public void createFields(final ClassCreator creator, final T info) {
        final PropertyDescriptor[] beans;
        try {
            beans = Introspector.getBeanInfo(info.getEntity())
                .getPropertyDescriptors();
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
        for (final PropertyDescriptor bean : beans) {
            if (!this.fieldAvailable(bean)) {
                continue;
            }
            for (final FieldGenerator<T> generator : this.createGenerators()) {
                if (generator.can(bean, info)) {
                    generator.generate(creator, bean, info);
                    break;
                }
            }
        }
    }

    private boolean fieldAvailable(final PropertyDescriptor bean) {
        final boolean available;
        if (bean.getReadMethod() != null) {
            available = !bean.getReadMethod().isDefault();
        } else if (bean.getWriteMethod() != null) {
            available = !bean.getWriteMethod().isDefault();
        } else {
            available = false;
        }
        return available;
    }

    protected abstract Collection<FieldGenerator<T>> createGenerators();

}
