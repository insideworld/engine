package insideworld.engine.security.amqp.auth;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.amqp.actions.AmqpReceiveProfile;
import insideworld.engine.amqp.actions.tags.AmqpTags;
import insideworld.engine.amqp.connection.Message;
import insideworld.engine.datatransfer.endpoint.actions.EndpointProfile;
import insideworld.engine.security.core.SecurityException;
import insideworld.engine.security.token.base.AbstractTokenAuth;
import insideworld.engine.security.token.base.data.TokenStorage;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmqpReceiveAuth extends AbstractTokenAuth {

    @Inject
    public AmqpReceiveAuth(final TokenStorage storage) {
        super(storage);
    }

    @Override
    protected final String getToken(Context parameter) throws SecurityException {
        final Message message = parameter.get(AmqpTags.MESSAGE);
        if (message == null) {
            throw new java.lang.SecurityException("Message is absent in context");
        } else {
            return (String) message.getProperties().get("Authorization");
        }
    }

    @Override
    public final Class<? extends EndpointProfile> forProfile() {
        return AmqpReceiveProfile.class;
    }
}
