package insideworld.engine.security.database.roles;

import insideworld.engine.database.AbstractCrudGenericStorage;
import insideworld.engine.database.AbstractCrudStorage;
import insideworld.engine.security.common.entities.Role;
import insideworld.engine.security.common.storages.RoleStorage;
import javax.inject.Singleton;

@Singleton
public class RoleCrud extends AbstractCrudGenericStorage<Role, RoleJpa> implements RoleStorage {

}

