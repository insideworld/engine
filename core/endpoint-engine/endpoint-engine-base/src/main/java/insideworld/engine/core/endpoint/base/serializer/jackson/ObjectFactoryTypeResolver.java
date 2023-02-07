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

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type resolver for Jackson.
 * Using object factory to find implementation of abstract type.
 *
 * Don't use here a cache because all of this is cached before.
 *
 * @since 2.0.0
 */
public class ObjectFactoryTypeResolver extends SimpleAbstractTypeResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectFactoryTypeResolver.class);

    private final ObjectFactory factory;

    /**
     * Default constructor.
     * Using simple HashMap here because all creation of readers are
     * @param factory Object factory.
     */
    public ObjectFactoryTypeResolver(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public JavaType findTypeMapping(DeserializationConfig config, JavaType type)
    {
        LOGGER.debug("Type resolver {} for type {}", Thread.currentThread().getName(), type.toString());
        final Class<?> implementation = this.factory.implementation(type.getRawClass());
        if (implementation == null) {
            return null;
        } else {
            return config.getTypeFactory().constructSpecializedType(type, implementation);
        }
    }

}
