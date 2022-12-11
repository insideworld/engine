package insideworld.engine.security.amqp.auth;

import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.amqp.actions.AmqpReceiveProfile;
import insideworld.engine.amqp.actions.tags.AmqpTags;
import insideworld.engine.security.token.base.AbstractTokenWrapper;
import insideworld.engine.security.token.base.TokenUserStorage;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AmqpReceiveAuth extends AbstractTokenWrapper {

    @Inject
    public AmqpReceiveAuth(final TokenUserStorage users) {
        super(users);
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(AmqpReceiveProfile.class);
    }

    @Override
    protected String getToken(final Context context) {
        return (String) context.get(AmqpTags.AMQP_PROPERTIES).get("token");
    }
}
