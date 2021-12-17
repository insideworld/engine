package insideworld.engine.security.common.entities;

import insideworld.engine.entities.Entity;

public interface Role extends Entity {

    String getName();

    Role getAppend();

}
