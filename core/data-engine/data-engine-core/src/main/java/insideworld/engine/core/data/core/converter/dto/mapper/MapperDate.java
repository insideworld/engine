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

import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.converter.dto.descriptors.Descriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.inject.Singleton;

/**
 * Map date value in string from record to entity and vice versa.
 * Can parse date from record to entity from string type by mask.
 *
 * @since 0.6.0
 */
@Singleton
public class MapperDate extends AbstractMapper<Object, Date> {

    /**
     * Mask for date.
     */
    private final ThreadLocal<SimpleDateFormat> format =
        ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        );

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        return descriptor.type().equals(Date.class);
    }

    @Override
    protected final Date toEntity(
        final Object target, final Descriptor descriptor) throws StorageException {
        final Date date;
        if (target == null) {
            date = null;
        } else if (Date.class.equals(target.getClass())) {
            date = (Date) target;
        } else {
            try {
                date = this.format.get().parse(target.toString());
            } catch (final ParseException exp) {
                throw new StorageException(
                    exp,
                    "Can't parse date for field %s of %s",
                    descriptor.name(), descriptor.parent()
                );
            }
        }
        return date;
    }

    @Override
    protected final Object toRecord(final Date value, final Descriptor descriptor) {
        return value;
    }

    @Override
    protected final String defineTag(final String origin) {
        return origin;
    }

}
