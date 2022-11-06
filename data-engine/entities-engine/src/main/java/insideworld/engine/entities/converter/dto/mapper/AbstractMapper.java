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
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Abstract mapper with generic methods which provide call setter or getter.
 *
 * @since 0.6.0
 */
public abstract class AbstractMapper implements Mapper {

    /**
     * Call write method from entity object.
     *
     * @param entity Entity.
     * @param target What needs to write.
     * @param descriptor Field descriptor.
     * @throws ActionException Can't write field.
     */
    protected static void write(
        final Entity entity, final Object target, final Descriptor descriptor)
        throws ActionException {
        try {
            final Method method = descriptor.method();
            if (method != null) {
                method.invoke(entity, target);
            }
        } catch (final
            IllegalAccessException
                | IllegalArgumentException
                    | InvocationTargetException exp) {
            throw new ActionException(
                String.format(
                    "Can't write field %s with type %s from %s",
                    descriptor.name(), descriptor.type(), entity.getClass()
                ),
                exp
            );
        }
    }

    /**
     * Call read method from entity object.
     *
     * @param entity Entity.
     * @param descriptor Field descriptor.
     * @return Field value.
     * @throws ActionException Can't read field.
     */
    protected static Object read(
        final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final Method method = descriptor.method();
        final Object target;
        if (method == null) {
            target = null;
        } else {
            try {
                target = method.invoke(entity);
            } catch (final
                IllegalAccessException
                    | IllegalArgumentException
                        | InvocationTargetException exp) {
                throw new ActionException(
                    String.format(
                        "Can't read field %s with type %s from %s",
                        descriptor.name(), descriptor.type(), entity.getClass()
                    ),
                    exp
                );
            }
        }
        return target;
    }
}
