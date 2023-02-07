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
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import insideworld.engine.core.endpoint.base.serializer.Serializer;
import insideworld.engine.core.endpoint.base.serializer.SerializerException;
import insideworld.engine.core.endpoint.base.serializer.types.Type;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer based on Jackson.
 * Using provided object reader and object write to serialize.
 * @since 2.0.0
 */
public class JacksonSerializer implements Serializer {

    private final ObjectReader reader;
    private final ObjectWriter writer;
    private final Type type;

    public JacksonSerializer(
        final ObjectMapper mapper,
        final JavaType jacksontype,
        final Type type
    ) {
        this.reader = mapper.readerFor(jacksontype);
        this.writer = mapper.writerFor(jacksontype);
        this.type = type;
    }

    @Override
    public void serialize(final Object value, final OutputStream stream) throws SerializerException {
        try {
            this.writer.writeValue(stream, value);
        } catch (final IOException exp) {
            throw new SerializerException(exp);
        }
    }

    @Override
    public Object deserialize(final InputStream stream)
        throws SerializerException {
        try {
            return this.reader.readValue(stream);
        } catch (final IOException exp) {
            throw new SerializerException(exp);
        }
    }

    @Override
    public Type forType() {
        return this.type;
    }
}
