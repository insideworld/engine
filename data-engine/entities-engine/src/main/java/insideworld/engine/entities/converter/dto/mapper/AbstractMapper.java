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
import insideworld.engine.exception.CommonException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;

/**
 * Abstract mapper with generic methods which provide call setter or getter.
 *
 * @param <D> DTO type to mapping.
 * @param <E> Entity field type to mapping.
 * @since 0.6.0
 */
public abstract class AbstractMapper<D, E> implements Mapper {

    @Override
    public final void toEntity(
        final Record record, final Entity entity, final Descriptor descriptor)
        throws CommonException {
        final String tag = this.defineTag(descriptor.name());
        if (record.contains(tag)) {
            AbstractMapper.write(entity, this.toEntity(record.get(tag), descriptor), descriptor);
        } else {
            AbstractMapper.write(entity, null, descriptor);
        }
    }

    @Override
    public final void toRecord(
        final Record record, final Entity entity, final Descriptor descriptor)
        throws CommonException {
        final D target = this.toRecord(
            (E) AbstractMapper.read(entity, descriptor),
            descriptor
        );
        if (target != null) {
            record.put(this.defineTag(descriptor.name()), target);
        }
    }

    /**
     * Convert from record to entity.
     * Using to bulk convert values which keeping in record to insert into entity.
     *
     * @param target Initial values.
     * @param descriptor Descriptor of field.
     * @return Collection of objects which write to entity object.
     * @throws CommonException Can't parse.
     */
    protected abstract E toEntity(D target, Descriptor descriptor)
        throws CommonException;

    /**
     * Convert from entity to record.
     * Using to bulk convert value from entity to record.
     *
     * @param value Collection from entity.
     * @param descriptor Descriptor of field.
     * @return Values for DTO.
     */
    protected abstract D toRecord(E value, Descriptor descriptor);

    /**
     * Define string key for record.
     *
     * @param origin Field name.
     * @return Record key.
     */
    protected abstract String defineTag(String origin);

    /**
     * Extract generic type from provided field.
     *
     * @param descriptor Field descriptor.
     * @return Generic type of collection.
     */
    protected static Class<?> getGeneric(final Descriptor descriptor) {
        return (Class<?>) ((ParameterizedType) descriptor.generic())
            .getActualTypeArguments()[0];
    }

    /**
     * Call write method from entity object.
     *
     * @param entity Entity.
     * @param target What needs to write.
     * @param descriptor Field descriptor.
     * @throws CommonException Can't write field.
     */
    private static void write(
        final Entity entity, final Object target, final Descriptor descriptor)
        throws CommonException {
        try {
            descriptor.method().invoke(entity, target);
        } catch (final
            IllegalAccessException
                | IllegalArgumentException
                    | InvocationTargetException exp) {
            throw new CommonException(
                exp,
                "Can't write field %s with type %s from %s",
                descriptor.name(), descriptor.type(), entity.getClass()
            );
        }
    }

    /**
     * Call read method from entity object.
     *
     * @param entity Entity.
     * @param descriptor Field descriptor.
     * @return Field value.
     * @throws CommonException Can't read field.
     */
    private static Object read(final Entity entity, final Descriptor descriptor)
        throws CommonException {
        try {
            return descriptor.method().invoke(entity);
        } catch (final
            IllegalAccessException
                | IllegalArgumentException
                    | InvocationTargetException exp) {
            throw new CommonException(
                exp,
                "Can't read field %s with type %s from %s",
                descriptor.name(), descriptor.type(), entity.getClass()
            );
        }
    }
}
