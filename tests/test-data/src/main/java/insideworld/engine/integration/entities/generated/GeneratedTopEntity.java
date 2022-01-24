package insideworld.engine.integration.entities.generated;

import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface GeneratedTopEntity extends Entity {

    String getMessage();

    void setMessage(String message);

    GeneratedChildEntity getChild();

    void setChild(GeneratedChildEntity child);

}
