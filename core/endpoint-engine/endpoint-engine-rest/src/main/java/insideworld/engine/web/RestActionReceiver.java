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

package insideworld.engine.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.datatransfer.endpoint.actions.AbstractActionReceiver;
import insideworld.engine.datatransfer.endpoint.actions.ContextBuilder;
import insideworld.engine.datatransfer.endpoint.actions.OutputTaskBuilder;
import insideworld.engine.web.tags.RestTags;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RestActionReceiver extends AbstractActionReceiver<RestParameter> {

    private final ObjectReader reader;

    /**
     * Default constructor.
     *
     * @param builder Output task builder.
     * @param executor Action executor.
     */
    @Inject
    public RestActionReceiver(final OutputTaskBuilder builder,
                              final ActionExecutor<String> executor) {
        super(builder, executor, RestProfile.class);
        this.reader = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @Override
    protected Collection<ContextBuilder> contextBuilders(final RestParameter parameter) {
        return Collections.singleton(() -> {
            final Context context = this.createContext();
            final Map<String, Object> map;
            try {
                map = this.reader.readValue(parameter.body());
            } catch (final IOException exp) {
                throw new RuntimeException(exp);
            }
            map.forEach(context::put);
            context.put(RestTags.HEADERS, parameter.headers());
            return context;
        });
    }
}
