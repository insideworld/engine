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

package insideworld.engine.entities.converter.dto.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WriteDescriptors extends AbstractDescriptors {

    @Inject
    public WriteDescriptors(final Collection<Mapper> mappers) {
        super(mappers);
    }

    @Override
    protected Descriptor createDescriptor(final PropertyDescriptor descriptor) {
        final Descriptor result;
        final Method method = descriptor.getWriteMethod();
        if (method == null || method.isAnnotationPresent(JsonIgnore.class)) {
            result = null;
        } else {
            result = new Descriptor(
                descriptor.getName(),
                method.getParameterTypes()[0],
                method.getGenericParameterTypes()[0],
                method
            );
        }
        return result;
    }
}
