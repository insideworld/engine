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

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Singleton;

@Singleton
public class MapperDate extends AbstractMapper {

    private final ThreadLocal<SimpleDateFormat> format =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    @Override
    public void toEntity(Record record, Entity entity, Descriptor descriptor) throws ActionException {
        final Object object = record.get(descriptor.getName());
        final Date date;
        if (object == null) {
            date = null;
        } else if (Date.class.equals(object.getClass())) {
            date = (Date) object;
        } else {
            try {
                date = this.format.get().parse(object.toString());
            } catch (final ParseException exp) {
                throw new ActionException("Can't parse date", exp);
            }
        }
        this.write(entity, date, descriptor);
    }

    @Override
    public void toRecord(Record record, Entity entity, Descriptor descriptor) throws ActionException {
        final Object target = this.read(entity, descriptor);
        if (target != null) {
            record.put(descriptor.getName(), target);
        }
    }

    @Override
    public boolean canApply(final Descriptor descriptor) {
        return descriptor.getType().equals(Date.class);
    }
}
