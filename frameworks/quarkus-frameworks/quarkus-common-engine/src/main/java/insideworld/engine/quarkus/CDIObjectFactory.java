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

package insideworld.engine.quarkus;

import insideworld.engine.injection.ObjectFactory;
import io.quarkus.arc.impl.Instances;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Singleton;

@Singleton
public class CDIObjectFactory implements ObjectFactory {

    @Override
    public <T> T createObject(final Class<T> type, final Object... args) {
        final Instance<T> select = CDI.current().select(type);
        if (select.isResolvable()) {
            return select.get();
        } else {
            return this.createNative(type, args);
        }
    }

    @Override
    public <T> T createObject(final TypeLiteral<T> type, final Object... args) {
        final Instance<T> select = CDI.current().select(type);
        if (select.isResolvable()) {
            return select.get();
        } else {
            return this.createNative(type.getRawType(), args);
        }
    }

    @Override
    public <T> Class<? extends T> implementation(final Class<T> type) {
        final var beans = Instances.resolveBeans(type);
        final Class<? extends T> result;
        if (beans.isEmpty()) {
            result = null;
        } else {
            final T object = this.createObject(type);
            result = (Class<? extends T>) object.getClass();
        }
        return result;
    }

    private <T> T createNative(final Class<T> type, final Object... args) {
        final Constructor<?> constructor = type.getDeclaredConstructors()[0];
        final Object[] input;
        final int count = constructor.getParameterCount();
        final Parameter[] parameters = constructor.getParameters();
        if (count == args.length) {
            input = args;
        } else {
            input = new Object[count];
            int custom = count - args.length - 1;
            for (int i = 0; i < input.length; i++) {
                if (i > custom) {
                    input[i] = args[i - custom - 1];
                } else {
                    input[i] = this.createObject(parameters[i].getType());
                }
            }
        }
        try {
            return (T) constructor.newInstance(input);
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
