package insideworld.engine.integration.entities.convertor;

import insideworld.engine.entities.Entity;

public interface TestOne extends Entity {
    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);
}