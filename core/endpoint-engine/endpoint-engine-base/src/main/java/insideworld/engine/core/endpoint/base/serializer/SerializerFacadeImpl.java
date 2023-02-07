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
import insideworld.engine.core.endpoint.base.serializer.types.Types;
import insideworld.engine.core.common.startup.OnStartUp;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class SerializerFacadeImpl implements SerializerFacade, OnStartUp {

    private final List<Types> types;
    private final List<SerializerFactory> factories;

    /**
     * Serializers calculated in init from provided types.
     */
    private final Map<Type, Serializer> serializers;

    /**
     * Serializers calculated in runtime.
     */
    private final Map<Type, Serializer> runtime;

    @Inject
    public SerializerFacadeImpl(
        final List<Types> types,
        final List<SerializerFactory> factories
    ) {
        this.types = types;
        this.factories = factories.stream()
            .sorted(Comparator.comparingLong(SerializerFactory::order).reversed())
            .toList();
        this.serializers = new HashMap<>(types.size());
        this.runtime = new ConcurrentHashMap<>();
    }

    @Override
    public Serializer getSerializer(final Type type) {
        return this.serializers.get(type);
    }

    @Override
    public Object deserialize(final InputStream stream, final Type type) throws CommonException {
        return this.serializers.get(type).deserialize(stream);
    }

    @Override
    public void serialize(final Object obj, final OutputStream stream) {

    }

    @Override
    public void startUp() throws CommonException {
        final Set<Type> all = this.types.stream()
            .flatMap(type -> type.getTypes().stream())
            .collect(Collectors.toSet());
        for (final Type type : all) {
            for (final SerializerFactory factory : this.factories) {
                if (factory.register(type)) {
                    break;
                }
            }
        }
        this.factories.stream()
            .flatMap(factory -> factory.create().stream())
            .forEach(serializer -> this.serializers.put(serializer.forType(), serializer));
    }

    @Override
    public long startOrder() {
        return 300_000;
    }
}
