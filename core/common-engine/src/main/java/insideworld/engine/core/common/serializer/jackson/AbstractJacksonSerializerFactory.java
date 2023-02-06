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

package insideworld.engine.core.common.serializer.jackson;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.serializer.Serializer;
import insideworld.engine.core.common.serializer.SerializerFactory;
import insideworld.engine.core.common.serializer.types.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

/**
 * Abstract serializer factory based on Jackson.
 *
 * @since 2.0.0
 */
public abstract class AbstractJacksonSerializerFactory implements SerializerFactory {

    /**
     * Set of types.
     */
    private final Set<Type> types = Sets.newHashSet();

    /**
     * Object factory.
     */
    private final ObjectFactory factory;
    private final List<JacksonTypeAdaptor> adaptors;

    @Inject
    public AbstractJacksonSerializerFactory(
        final ObjectFactory factory,
        final List<JacksonTypeAdaptor> adaptors
    ) {
        this.factory = factory;
        this.adaptors = adaptors.stream()
            .sorted(Comparator.comparingLong(JacksonTypeAdaptor::order).reversed())
            .toList();
    }

    @Override
    public final boolean register(final Type type) {
        final boolean can;
        if (this.can(type)) {
            this.types.add(type);
            can = true;
        } else {
            can = false;
        }
        return can;
    }

    @Override
    public final Collection<Serializer> create() {
        final var mapper = new ObjectMapper();
        final var module = new SimpleModule();
        this.addInterfaces(module);
        this.modifyModule(module);
        mapper.registerModule(module);
        mapper.registerModule(new BlackbirdModule());
        final Collection<Serializer> serializers = Lists.newArrayListWithCapacity(
            this.types.size()
        );
        for (final Type type : this.types) {
            for (final JacksonTypeAdaptor adaptor : this.adaptors) {
                final JavaType convert = adaptor.convert(mapper, type);
                if (convert != null) {
                    serializers.add(new JacksonSerializer(mapper, convert, type));
                    break;
                }
            }
        }
        return serializers;
    }

    protected abstract boolean can(final Type type);

    protected abstract void modifyModule(SimpleModule module);

    /**
     * Add interfaces implementation to module.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addInterfaces(final SimpleModule module) {
        final Map<Class, Class> interfaces = this.types.stream()
            .map(Type::getType)
            .filter(Class::isInterface)
            .distinct()
            .filter(type -> this.factory.implementation(type) != null)
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    this.factory::implementation
                )
            );
        for (final var entry : interfaces.entrySet()) {
            module.addAbstractTypeMapping(entry.getKey(), entry.getValue());
        }
    }
}
