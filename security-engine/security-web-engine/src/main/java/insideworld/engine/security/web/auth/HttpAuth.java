package insideworld.engine.security.web.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.security.common.auth.Auth;
import insideworld.engine.security.common.auth.TokenContainer;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.HttpHeaders;

@Singleton
public class HttpAuth implements PreExecute<HttpHeaders> {

    private final Auth<TokenContainer> auth;

    @Inject
    public HttpAuth(final Auth<TokenContainer> auth) {
        this.auth = auth;
    }

    @Override
    public void preExecute(final Record context, final HttpHeaders parameter) throws Exception {
        this.auth.performAuth(context, () -> parameter.getHeaderString("token"));
    }
}
