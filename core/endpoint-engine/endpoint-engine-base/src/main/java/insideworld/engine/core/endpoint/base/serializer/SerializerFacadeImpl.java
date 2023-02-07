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

package insideworld.engine.core.endpoint.base.serializer;

import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import insideworld.engine.core.endpoint.base.serializer.types.factory.TypeFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.SerializationException;

/**
 *
 */
@Singleton
public class SerializerFacadeImpl implements SerializerFacade {

    /**
     * Factories.
     */
    private final List<SerializerFactory> factories;

    /**
     * Type factory.
     */
    private final List<TypeFactory> types;

    /**
     * Serializers cache.
     */
    private final Map<Type, Serializer> serializers;

    @Inject
    public SerializerFacadeImpl(
        final List<SerializerFactory> factories,
        final List<TypeFactory> types
    ) {
        this.factories = factories.stream()
            .sorted(Comparator.comparingLong(SerializerFactory::order).reversed())
            .toList();
        this.types = types.stream()
            .sorted(Comparator.comparingLong(TypeFactory::order).reversed())
            .toList();
        this.serializers = new ConcurrentHashMap<>();
    }

    @Override
    public Serializer getSerializer(final Type type) {
        Serializer serializer = this.serializers.get(type);
        if (serializer == null) {
            synchronized (this) {
                serializer = this.serializers.get(type);
                if (serializer == null) {
                    for (final SerializerFactory factory : this.factories) {
                        if (factory.can(type)) {
                            serializer = factory.create(type);
                            this.serializers.put(type, serializer);
                            break;
                        }
                    }
                }
            }
        }
        return serializer;
    }

    @Override
    public Object deserialize(final Type type, final InputStream stream) throws CommonException {
        return this.getSerializer(type).deserialize(stream);
    }

    @Override
    public void serialize(final Object obj, final OutputStream stream) throws CommonException {
        final Type type = this.types.stream()
            .filter(factory -> factory.can(obj))
            .findFirst()
            .map(factory -> factory.create(obj))
            .orElseThrow(() -> new SerializationException("Can't create type"));
        this.getSerializer(type).serialize(obj, stream);
    }
}
