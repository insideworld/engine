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

package insideworld.engine.core.data.core.converter.dto.mapper;

import com.google.common.primitives.Primitives;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.converter.dto.descriptors.Descriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javax.inject.Singleton;

/**
 * Mapper for collection of primitives.
 * Logic is easy - the same in record, the same in entity.
 * Tag is the same.
 * @since 0.14.0
 */
@Singleton
public class MapperPrimitives extends AbstractMapper<Collection<Object>, Collection<Object>> {

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        final boolean can;
        if (Collection.class.isAssignableFrom(descriptor.type())) {
            final Class<?> generic = this.getGeneric(descriptor);
            can = generic.equals(String.class) || Primitives.isWrapperType(generic);
        } else {
            can = false;
        }
        return can;
    }

    @Override
    protected final Collection<Object> toEntity(
        final Collection<Object> target, final Descriptor descriptor) throws StorageException {
        return Objects.requireNonNullElse(target, Collections.emptyList());
    }

    @Override
    protected final Collection<Object> toRecord(
        final Collection<Object> value, final Descriptor descriptor) {
        return value;
    }

    @Override
    protected final String defineTag(final String origin) {
        return origin;
    }
}
