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

package insideworld.engine.core.endpoint.base.serializer.jackson.adaptors;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Deprecated because for array needs type mappings and we won't use arrays in the system.
 *
 * What is problem:
 * I don't know how to add abstract type mapping in runtime and array type is specific.
 * Maybe need to think about generator for linking interfaces to implementation throw Mixin....
 * But now I don't see neccessary to use array for ser/deser operation and I can use a collection.
 *
 * @deprecated
 */
@Deprecated
public class ArrayTypeAdaptor implements JacksonTypeAdaptor {

    private final ObjectFactory factory;

    @Inject
    public ArrayTypeAdaptor(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public JavaType convert(final ObjectMapper mapper, final Type type) {
        final Class<?> implementation;
        if (type.getOrigin().getComponentType().isInterface()) {
            implementation = this.factory.implementation(type.getOrigin().getComponentType());
        } else {
            implementation = type.getOrigin().getComponentType();
        }
        return mapper.getTypeFactory().constructArrayType(implementation);
    }

    @Override
    public boolean can(final Type type) {
        return type.getOrigin().isArray();
    }

    @Override
    public int order() {
        return 200_000;
    }
}
