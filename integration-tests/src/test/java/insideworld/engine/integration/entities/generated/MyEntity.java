package insideworld.engine.integration.entities.generated;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;

@GenerateEntity(type = "JPA")
@GenerateStorage(type = "JPA")
public interface MyEntity extends Entity {

    String getMessage();

    void setMessage(String message);

}
