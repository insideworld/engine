package insideworld.engine.data.generator.jpa.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public interface ActionInfo {

    Class<? extends Entity> entity();

    String key();

    Class<?>[] interfaces();

}
