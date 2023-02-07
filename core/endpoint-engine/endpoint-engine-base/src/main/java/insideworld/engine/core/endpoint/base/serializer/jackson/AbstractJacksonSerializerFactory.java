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

package insideworld.engine.core.endpoint.base.serializer.jackson;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.endpoint.base.serializer.Serializer;
import insideworld.engine.core.endpoint.base.serializer.SerializerFactory;
import insideworld.engine.core.endpoint.base.serializer.jackson.adaptors.JacksonTypeAdaptor;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.SerializationException;

/**
 * Abstract serializer factory based on Jackson.
 *
 * @since 2.0.0
 */
public abstract class AbstractJacksonSerializerFactory implements SerializerFactory {

    private final List<JacksonTypeAdaptor> adaptors;
    private final ObjectFactory factory;

    private final ObjectMapper mapper;

    @Inject
    public AbstractJacksonSerializerFactory(
        final List<JacksonTypeAdaptor> adaptors,
        final ObjectFactory factory
        ) {
        this.adaptors = adaptors.stream()
            .sorted(Comparator.comparingLong(JacksonTypeAdaptor::order).reversed())
            .toList();
        this.factory = factory;
        this.mapper = new ObjectMapper();
        final var module = new SimpleModule();
        this.modifyModule(module);
        module.setAbstractTypes(new ObjectFactoryTypeResolver(this.factory));
        this.mapper.registerModule(module);
        this.mapper.registerModule(new BlackbirdModule());
    }

    @Override
    public final Serializer create(final Type type) {
        final JavaType javatype = this.adaptors.stream()
            .filter(adaptor -> adaptor.can(type))
            .findFirst()
            .map(adaptor -> adaptor.convert(this.mapper, type))
            .orElseThrow(
                () -> new SerializationException("Adaptor not found. It's shouldn't be...")
            );

//        final Class<?> qwe = this.extractBaseType(javatype);
//        if (qwe.isInterface()) {
//            this.mapper.registerSubtypes(
//                this.factory.implementation(
//                    qwe
//                )
//            );
//        }
        return new JacksonSerializer(this.mapper, javatype, type);
    }

    protected abstract void modifyModule(SimpleModule module);

    private Class<?> extractBaseType(final JavaType type) {
        if (type.getContentType() == null) {
            return type.getRawClass();
        } else {
            return type.getContentType().getRawClass();
        }
    }
}
