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

package insideworld.engine.core.endpoint.base.action.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import java.io.IOException;

public class EntityDeserializer<T extends Entity>
    extends StdDeserializer<T>
    implements ResolvableDeserializer {

    private final JsonDeserializer<?> def;
    private final Storage<T> storage;

    public EntityDeserializer(final JsonDeserializer<?> def,
                              final Class<?> clazz,
                              final Storage<T> storage
    ) {
        super(clazz);
        this.def = def;
        this.storage = storage;
    }

    @Override
    public T deserialize(final JsonParser parser, final DeserializationContext context)
        throws IOException {
        final T result;
        if (parser.getCurrentToken().isStructStart()) {
            result = (T) this.def.deserialize(parser, context);
        } else {
            final JsonNode node = parser.getCodec().readTree(parser);
            try {
                result = this.storage.read(node.longValue());
            } catch (final StorageException exp) {
                throw new IOException(exp);
            }
        }
        return result;
    }

    @Override
    public void resolve(DeserializationContext context) throws JsonMappingException {
        ((ResolvableDeserializer) def).resolve(context);
    }
}