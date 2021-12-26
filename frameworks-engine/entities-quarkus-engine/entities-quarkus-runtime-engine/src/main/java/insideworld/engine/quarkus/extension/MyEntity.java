package insideworld.engine.quarkus.extension;

import insideworld.engine.entities.Entity;

public interface MyEntity extends Entity {

    String getMessage();

    void setMessage(String message);

}
