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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import insideworld.engine.core.data.core.Entity;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class JacksonSerializer<T extends Entity>
    extends StdSerializer<T>
    implements ResolvableSerializer {

    private final JsonSerializer<T> def;

    public JacksonSerializer(final JsonSerializer<?> def, final Class<T> clazz) {
        super(clazz);
        this.def = (JsonSerializer<T>) def;
    }

    @Override
    public void resolve(final SerializerProvider provider) throws JsonMappingException {
        ((ResolvableSerializer) this.def).resolve(provider);
    }

    @Override
    public void serialize(final T value, final JsonGenerator gen, final SerializerProvider provider)
        throws IOException {
        Class<?> top = (Class<?>) provider.getAttribute("top");
        if (top == null) {
            top = value.getClass();
            provider.setAttribute("top", top);
        }
        if (top.equals(value.getClass())) {
            this.def.serialize(value, gen, provider);
        } else {
            gen.writeNumber(value.getId());
        }
    }
}
