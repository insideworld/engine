package insideworld.engine.security.common.entities;

import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface User extends Entity {

    String getToken();

    void setToken(String token);

    Role getRole();

    void setRole(Role role);

    String getName();

    void setName(String name);

    Collection<Role> getAvailableRoles();
}
