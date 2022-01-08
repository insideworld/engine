package insideworld.engine.security.database.roles;

import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import java.util.Optional;
import javax.inject.Singleton;
import javax.naming.AuthenticationException;

@Singleton
public class UserCrud extends AbstractCrudGenericStorage<User, UserJpa> implements UserStorage {

    @Override
    public User getByToken(final String token) throws AuthenticationException {
        return find("token", token)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResultOptional()
            .orElseThrow(() -> new AuthenticationException("Token not found"));
    }

    @Override
    public Optional<User> getByName(final String name) {
        return Optional.ofNullable(find("name", name)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResult());
    }
}
