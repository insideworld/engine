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
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.datatransfer.endpoint.actions.ActionReceiver;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.threads.ThreadPool;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;

@Singleton
public class RestActionReceiver {

    private final ObjectReader reader;

    private final;

    private final ObjectFactory factory;

    @Inject
    public RestActionReceiver(final ObjectFactory factory,
                              final ActionR eceiver<ReceiveParameters> receiver) {
        this.factory = factory;
        this.reader = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
        this.receiver = this.factory.createObject(new TypeLiteral<>() { });
    }

    @POST
    @Path("/{action}")
    @Consumes("application/json")
    @Produces("application/json")
    public Output executeAction(
        @PathParam("action") final String action,
        @javax.ws.rs.core.Context final HttpHeaders headers,
        final InputStream rawbody
    ) throws IOException, CommonException {
        final var map = (Map<String, Object>) this.reader.readValue(rawbody);
        final var parameter = this.factory.createObject(ReceiveParameters.class, headers);
        return this.receiver.executeSingle(action, parameter, map);
    }

}
