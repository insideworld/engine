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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.google.common.collect.Maps;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.startup.OnStartUp;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
import insideworld.engine.core.endpoint.base.serializer.Types;
import insideworld.engine.core.endpoint.base.serializer.Serializer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EntitySerializer implements Serializer, OnStartUp {

    private final StorageKeeper keeper;

    private final ObjectMapper mapper;

    private final Map<Class<? extends Entity>, ObjectReader> readers;

    private final Map<Class<? extends Entity>, ObjectWriter> writers;
    private final Types types;

    @Inject
    public EntitySerializer(
        final StorageKeeper keeper,
        final Types types
    ) {
        this.keeper = keeper;
        this.readers = Maps.newHashMapWithExpectedSize(keeper.getAllStorage().size());
        this.writers = Maps.newHashMapWithExpectedSize(keeper.getAllStorage().size());
        this.types = types;
        this.mapper = new ObjectMapper();
    }

    @Override
    public <T> void serialize(final T value, final Class<?> type, final OutputStream stream) {
        try {
            this.writers.get(type).writeValue(stream, value);
        } catch (final IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public <T> T deserialize(final InputStream stream, final Class<?> type) {
        try {
            return this.readers.get(type).readValue(stream);
        } catch (final IOException exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public boolean applicable(final Class<?> type) {
        return Entity.class.isAssignableFrom(type);
    }

    @Override
    public long order() {
        return 100;
    }

    @Override
    public void startUp() throws CommonException {
        final SimpleModule module = new SimpleModule();
        for (final Storage<? extends Entity> storage : this.keeper.getAllStorage()) {
            final Class<? extends Entity> type = storage.forEntity();
            module.addAbstractTypeMapping((Class) type, this.keeper.implementation(type));
        }
        module.setDeserializerModifier(this.createDeserializerModifier());
        module.setSerializerModifier(this.createSerializerModifier());
        this.mapper.registerModule(module);
        for (final Class<?> type : this.types.getInputs()) {
            if (this.applicable(type)) {
                this.readers.put((Class<? extends Entity>) type, this.mapper.readerFor(type));
            }
        }
        for (final Class<?> type : this.types.getOutputs()) {
            if (this.applicable(type)) {
                this.writers.put((Class<? extends Entity>) type, this.mapper.writerFor(type));
            }
        }
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
                if (applicable(description.getBeanClass())) {
                    final Class<? extends Entity> type =
                        (Class<? extends Entity>) description.getBeanClass();
                    try {
                        result = new JacksonDeserializer<>(
                            deserializer,
                            type,
                            keeper.getStorage(type)
                        );
                    } catch (StorageException e) {
                        throw new RuntimeException(e);
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
                if (applicable(description.getBeanClass())) {
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

    @Override
    public long startOrder() {
        return 900_000;
    }
}
