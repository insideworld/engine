package insideworld.engine.integration.entities.generated;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;

import javax.persistence.Table;

@GenerateEntity(type = "JPA")
@GenerateStorage(type = "JPA")
@Table(schema = "generated", name = "top")
public interface GeneratedTopEntity extends Entity {

    String getMessage();

    void setMessage(String message);

    GeneratedChildEntity getChild();

    void setChild(GeneratedChildEntity child);

}
