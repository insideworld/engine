package insideworld.engine.integration.entities;

import insideworld.engine.entities.Entity;

public interface JpaArray extends Entity {
    String getMessage();

    long getMainId();

    void setMainId(long mainId);
}
