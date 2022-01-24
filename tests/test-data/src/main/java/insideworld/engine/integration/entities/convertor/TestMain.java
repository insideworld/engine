package insideworld.engine.integration.entities.convertor;

import insideworld.engine.entities.Entity;
import java.util.Collection;

public interface TestMain extends Entity {
    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    TestOne getOne();

    void setOne(TestOne one);

    TestTwo getTwo();

    void setTwo(TestTwo two);

    Collection<TestArray> getArrays();

    void setArrays(Collection<TestArray> arrays);
}
