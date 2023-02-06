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

package insideworld.engine.core.action.serializer;

import insideworld.engine.core.common.serializer.types.Type;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Type based on method.
 *
 * @since 2.0.0
 */
public class MethodType implements Type {

    private final Class<?> raw;

    private final Class<?> wrapped;

    public MethodType(final Method method, final int idx) {
        this.raw = method.getParameterTypes()[idx];
        if (method.getGenericParameterTypes()[idx] instanceof ParameterizedType) {
            final java.lang.reflect.Type type = ((ParameterizedType) method
                .getGenericParameterTypes()[idx])
                .getActualTypeArguments()[0];
            this.wrapped = (Class<?>) type;
        } else {
            this.wrapped = null;
        }
    }

    @Override
    public Class<?> getOrigin() {
        return this.raw;
    }

    @Override
    public Class<?> getWrapped() {
        return this.wrapped;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MethodType that = (MethodType) obj;
        if (!this.raw.equals(that.raw)) {
            return false;
        }
        return Objects.equals(this.wrapped, that.wrapped);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(this.raw)
            .append(this.wrapped)
            .toHashCode();
    }
}
