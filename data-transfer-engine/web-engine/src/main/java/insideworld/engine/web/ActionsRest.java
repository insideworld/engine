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
import insideworld.engine.actions.facade.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rest enpoint for actions.
 *
 * @since 0.0.5
 */
@Path("/actions")
public class ActionsRest implements ActionsEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(ActionsRest.class);

    private final ActionExecutor<String> executor;
    private final Collection<TagHandler> handlers;
    private final Collection<PreExecute<HttpHeaders>> pre;
    private final ObjectReader reader;

    @Inject
    public ActionsRest(
        final ActionExecutor<String> executor,
        final Collection<TagHandler> handlers,
        final Collection<PreExecute<HttpHeaders>> pre) {
        this.executor = executor;
        this.handlers = handlers;
        this.pre = pre;
        this.reader = new ObjectMapper()
            .readerFor(Map.class)
            .with(DeserializationFeature.USE_LONG_FOR_INTS);
    }

    @POST
    @Path("/{action}")
    @Consumes("application/json")
    @Produces("application/json")
    public Output executeAction(
        @PathParam("action") final String action,
        @javax.ws.rs.core.Context HttpHeaders headers,
        final InputStream rawbody
    ) {
        try {
            return this.executeAction(
                action, headers, (Map<String, Object>) this.reader.readValue(rawbody));
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    @Override
    public Output executeAction(final String action,
                                final HttpHeaders headers,
                                final Map<String, Object> body) throws Exception {
        final Context context = this.executor.createContext();
        body.forEach(context::put);
        for (final PreExecute<HttpHeaders> execute : this.pre) {
            execute.preExecute(context, headers);
        }
        try {
            for (final TagHandler handler : this.handlers) {
                handler.perform(context);
            }
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
        return this.executor.execute(action, context);
    }
}
