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

package insideworld.engine.entities.converter.dto.mapper;

import com.google.common.base.Defaults;
import com.google.common.primitives.Primitives;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import javax.inject.Singleton;

/**
 * Map primitives to record from entity and vice versa.
 * Using for single primitive.
 * Logic is easy - the same in record, the same in entity.
 * In case if convert primitive with null value to entity - use default value.
 * In case if convert primitive with default value to record - set null.
 *
 * @since 0.0.1
 */
@Singleton
public class MapperPrimitive extends AbstractMapper<Object, Object> {

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        final Class<?> type = descriptor.type();
        return type.isPrimitive() || type.equals(String.class) || Primitives.isWrapperType(type);
    }

    @Override
    protected final Object toEntity(final Object target, final Descriptor descriptor)
        throws StorageException {
        final Object result;
        if (target == null && descriptor.type().isPrimitive()) {
            result = Defaults.defaultValue(descriptor.type());
        } else {
            result = target;
        }
        return result;
    }

    @Override
    protected final Object toRecord(final Object value, final Descriptor descriptor) {
        final Object result;
        if (descriptor.type().isPrimitive()
            && Defaults.defaultValue(descriptor.type()).equals(value)
        ) {
            result = null;
        } else {
            result = value;
        }
        return result;
    }

    @Override
    protected final String defineTag(final String origin) {
        return origin;
    }

}