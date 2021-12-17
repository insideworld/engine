package insideworld.engine.security.amqp.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.amqp.AmqpMessageBuilder;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.AuthenticationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class AmqpSendAuth implements PreExecute<AmqpMessageBuilder> {

    private final User user;

    @Inject
    public AmqpSendAuth(final PropertiesProvider properties,
                        final UserStorage storage)
        throws AuthenticationException, PropertiesException {
        final String username = properties.provide("engine.amqp.username", String.class);
        this.user = storage.getByName(username).orElseThrow(
            () -> new AuthenticationException("User for AMQP auth is not found")
        );
    }

    @Override
    public void preExecute(final Record context, final AmqpMessageBuilder parameter) throws Exception {
        parameter.applicationProperties(new JsonObject().put("token", this.user.getToken()));
    }
}
