package insideworld.engine.security.amqp.auth;

import insideworld.engine.amqp.connection.Message;
import insideworld.engine.security.token.base.AbstractTokenAuth;
import insideworld.engine.security.token.base.data.TokenStorage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmqpReceiveAuth extends AbstractTokenAuth<Message> {

    @Inject
    public AmqpReceiveAuth(final TokenStorage storage) {
        super(storage);
    }

    @Override
    protected final String getToken(final Message parameter) {
        return (String) parameter.getProperties().get("Authorization");
    }
}
