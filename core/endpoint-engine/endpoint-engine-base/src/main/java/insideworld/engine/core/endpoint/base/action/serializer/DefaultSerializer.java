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

package insideworld.engine.core.endpoint.base.action.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;

@Singleton
public class DefaultSerializer implements Serializer {

    private final ObjectMapper mapper;

    private final Map<Class<?>, ObjectReader> readers;

    private final Map<Class<?>, ObjectWriter> writers;

    public DefaultSerializer() {
        this.mapper = new ObjectMapper();
        this.readers = new ConcurrentHashMap<>();
        this.writers = new ConcurrentHashMap<>();
    }

    @Override
    public <T> void serialize(final T value, final Class<?> type, final OutputStream stream) {
        final ObjectWriter writer;
        if (this.writers.containsKey(type)) {
            writer = this.writers.get(type);
        } else {
            synchronized (this) {
                if (this.writers.containsKey(type)) {
                    writer = this.writers.get(type);
                } else {
                    writer = this.mapper.writerFor(type);
                    this.writers.put(type, writer);
                }
            }
        }
        try {
            writer.writeValue(stream, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(final InputStream stream, final Class<?> type) {
        final ObjectReader reader;
        if (this.readers.containsKey(type)) {
            reader = this.readers.get(type);
        } else {
            synchronized (this) {
                if (this.readers.containsKey(type)) {
                    reader = this.readers.get(type);
                } else {
                    reader = this.mapper.readerFor(type);
                    this.readers.put(type, reader);
                }
            }
        }
        try {
            return reader.readValue(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean applicable(final Class<?> type) {
        return true;
    }

    @Override
    public long order() {
        return 0;
    }
}
