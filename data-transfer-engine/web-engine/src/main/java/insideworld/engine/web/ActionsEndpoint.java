package insideworld.engine.web;

import insideworld.engine.actions.keeper.output.Output;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.HttpHeaders;

/**
 * Interface for interact with actions.
 *
 * @since 0.0.5
 */
public interface ActionsEndpoint {

    Output executeAction(String action, HttpHeaders token, Map<String, Object> body)
        throws Exception;

}
