package insideworld.engine.entities;

import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;



@GenerateEntity(type = "JPA")
@GenerateStorage(type = "JPA")
//@Table(name = "generated", schema = "entities")
public interface MyEntity extends Entity {

    String getMessage();

    void setMessage(String message);

}
