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

package insideworld.engine.core.endpoint.data;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.endpoint.base.serializer.jackson.AbstractJacksonSerializerFactory;
import insideworld.engine.core.endpoint.base.serializer.jackson.adaptors.JacksonTypeAdaptor;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntitySerializerFactory extends AbstractJacksonSerializerFactory {

    private final StorageKeeper keeper;

    @Inject
    public EntitySerializerFactory(
        final List<JacksonTypeAdaptor> adaptors,
        final StorageKeeper keeper
    ) {
        super(adaptors);
        this.keeper = keeper;
    }

    @Override
    public long order() {
        return 100;
    }


    @Override
    public boolean can(final Type type) {
        return this.can(type.getType());
    }

    protected boolean can(final Class<?> type) {
        return Entity.class.isAssignableFrom(type);
    }

    @Override
    protected void modifyModule(final SimpleModule module) {
        module.setDeserializerModifier(this.createDeserializerModifier());
        module.setSerializerModifier(this.createSerializerModifier());
    }

    private BeanDeserializerModifier createDeserializerModifier() {
        return new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyDeserializer(
                final DeserializationConfig config,
                final BeanDescription description,
                final JsonDeserializer<?> deserializer
            ) {
                final JsonDeserializer<?> result;
                if (can(description.getBeanClass())) {
                    final Class<? extends Entity> type =
                        (Class<? extends Entity>) description.getBeanClass();
                    try {
                        result = new JacksonDeserializer<>(
                            deserializer,
                            type,
                            keeper.getStorage(type)
                        );
                    } catch (final StorageException exp) {
                        throw new RuntimeException(exp);
                    }
                } else {
                    result = deserializer;
                }
                return result;
            }
        };
    }

    private BeanSerializerModifier createSerializerModifier() {
        return new BeanSerializerModifier() {
            @Override
            public JsonSerializer<?> modifySerializer(
                final SerializationConfig config,
                final BeanDescription description,
                final JsonSerializer<?> serializer
            ) {
                final JsonSerializer<?> result;
                if (can(description.getBeanClass())) {
                    result = new JacksonSerializer<>(
                        serializer,
                        (Class<? extends Entity>) description.getBeanClass()
                    );
                } else {
                    result = serializer;
                }
                return result;
            }
        };
    }
}
