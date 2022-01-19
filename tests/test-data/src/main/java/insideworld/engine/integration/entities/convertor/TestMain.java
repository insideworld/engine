package insideworld.engine.integration.entities.convertor;

import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface TestMain extends Entity {
    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    JpaTestOne getOne();

    void setOne(JpaTestOne one);

    JpaTestTwo getTwo();

    void setTwo(JpaTestTwo two);

    Collection<JpaArray> getArrays();

    void setArrays(Collection<JpaArray> arrays);
}
