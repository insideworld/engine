package insideworld.engine.web;

import java.net.URI;
import java.net.URISyntaxException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * NotFoundExepptionMapper
 */
public abstract class AbstractRedirectMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public final Response toResponse(NotFoundException exception) {
        try {
            return Response.temporaryRedirect(new URI(this.url())).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(exception);
        }
    }

    protected abstract String url();
}