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

package insideworld.engine.core.endpoint.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Maps;
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.ExecutorTags;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.AbstractExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class RestWrapper extends AbstractExecuteWrapper {

    private final Map<Class<?>, ObjectReader> readers = Maps.newConcurrentMap();

    @Override
    public final void execute(final ExecuteContext context) throws CommonException {
        final ObjectReader reader = this.getReader(context.get(ExecutorTags.ACTION).inputType());
        try {
            final Object result = reader.readValue((InputStream) context.get(ExecutorTags.INPUT));
            context.put(ExecutorTags.INPUT, result, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.execute(context);
    }


    private ObjectReader getReader(final Class<?> clazz) {
        final ObjectReader result;
        if (this.readers.containsKey(clazz)) {
            result = this.readers.get(clazz);
        } else {
            synchronized (this) {
                if (this.readers.containsKey(clazz)) {
                    result = this.readers.get(clazz);
                } else {
                    result = new ObjectMapper()
                        .readerFor(clazz)
                        .with(DeserializationFeature.USE_LONG_FOR_INTS);
                    this.readers.put(clazz, result);
                }
            }
        }
        return result;
    }

    @Override
    public long wrapperOrder() {
        return 900_000;
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(RestProfile.class);
    }
}
