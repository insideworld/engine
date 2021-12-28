package insideworld.engine.integration.entities.generated;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;

import javax.persistence.Column;
import javax.persistence.Table;

@GenerateEntity(type = "JPA")
@GenerateStorage(type = "JPA")
@Table(name = "generated", schema = "entities")
public interface MyEntity extends Entity {

    String getMessage();

    void setMessage(String message);

}
