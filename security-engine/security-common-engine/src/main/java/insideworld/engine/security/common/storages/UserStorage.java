package insideworld.engine.security.common.storages;

import insideworld.engine.entities.storages.Storage;
import insideworld.engine.security.common.entities.User;
import java.util.Collection;
import java.util.Optional;
import javax.naming.AuthenticationException;

public interface UserStorage extends Storage<User> {

    User getByToken(String token) throws AuthenticationException;

    Optional<User> getByName(String name);

    Collection<User> getByNames(Collection<String> name);

}
