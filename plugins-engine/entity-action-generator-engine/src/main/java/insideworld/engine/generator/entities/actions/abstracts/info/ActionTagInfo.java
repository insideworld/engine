package insideworld.engine.generator.entities.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public interface ActionTagInfo {

    Class<? extends Entity> entity();

    String key();

    Class<?>[] interfaces();

    String tag();

    String implementation();

}
