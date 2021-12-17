package insideworld.engine.security.amqp.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.security.common.auth.Auth;
import insideworld.engine.security.common.auth.TokenContainer;
import io.vertx.mutiny.amqp.AmqpMessage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmqpReceiveAuth implements PreExecute<AmqpMessage> {

    private final Auth<TokenContainer> auth;

    @Inject
    public AmqpReceiveAuth(final Auth<TokenContainer> auth) {
        this.auth = auth;
    }

    @Override
    public void preExecute(final Record context, final AmqpMessage parameter)
        throws Exception {
        this.auth.performAuth(
            context,
            () -> parameter.applicationProperties().getString("token")
        );
    }
}
