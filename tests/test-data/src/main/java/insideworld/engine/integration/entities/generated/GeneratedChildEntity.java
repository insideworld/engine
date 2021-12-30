package insideworld.engine.integration.entities.generated;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.generate.GenerateEntity;
import insideworld.engine.entities.generate.GenerateStorage;
import javax.persistence.Table;

@GenerateEntity(type = "JPA")
@GenerateStorage(type = "JPA")
@Table(schema = "generated", name = "child")
public interface GeneratedChildEntity extends Entity {

    String getSome();

    void setSome(String some);

}
