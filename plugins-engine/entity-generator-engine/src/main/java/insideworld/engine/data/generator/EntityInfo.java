package insideworld.engine.data.generator;

import insideworld.engine.entities.Entity;

public interface EntityInfo {

    Class<? extends Entity> getEntity();

    String getImplementation();
}
