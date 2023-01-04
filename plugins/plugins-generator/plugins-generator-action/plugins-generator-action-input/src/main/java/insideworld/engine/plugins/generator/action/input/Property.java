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

package insideworld.engine.plugins.generator.action.input;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;

public class Property {

    private final String name;
    private final List<Method> methods;

    private Class<?> getter;

    private Class<?> setter;

    public Property(final String name, final List<Method> methods) {
        this.name = name;
        this.methods = methods;
    }

    public final void init(final StringBuilder builder) {
        this.getter = this.getterType();
        this.setter = this.setterType();
        this.checkUnique(builder);
        this.checkType(builder);
    }

    public String getName() {
        return this.name;
    }

    public Class<?> getType() {
        return ObjectUtils.defaultIfNull(this.setter, this.getter);
    }

    public Optional<Class<?>> getGetter() {
        return Optional.ofNullable(this.getter);
    }

    public Optional<Class<?>> getSetter() {
        return Optional.ofNullable(this.setter);
    }

    private Class<?> getterType() {
        return this.methods.stream().filter(method -> method.getName().startsWith(Properties.GET))
            .map(Method::getReturnType)
            .findFirst()
            .orElse(null);
    }

    private Class<?> setterType() {
        return this.methods.stream()
            .filter(method -> method.getName().startsWith(Properties.SET)
                              && method.getParameterCount() == 1)
            .map(method -> method.getParameterTypes()[0])
            .findFirst()
            .orElse(null);
    }

    private void checkType(final StringBuilder builder) {
        if (ObjectUtils.allNotNull(this.getter, this.setter)
            && ObjectUtils.notEqual(this.getter, this.setter)
        ) {
            builder.append("Property ").append(this.name)
                .append(" has different types of getter and setter for property:\n\r");
            builder.append(this.getter.getName()).append(" and ").append(this.setter.getName());
        }
    }

    /**
     * Check that grouped methods has no more than 2 method (getter and setter).
     * Raise a runtime exception.
     */
    private void checkUnique(final StringBuilder builder) {
        if (this.methods.size() > 2 ) {
            builder.append("Property ").append(this.name).append(" has more than 2 method:\n\r");
            for (final Method method : this.methods) {
                builder.append("* ").append(method.toString()).append("\n\r");
            }
        }
    }
}
