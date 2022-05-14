package insideworld.engine.jobs.common.entity;

import insideworld.engine.entities.Entity;

public interface JobEntity extends Entity {

    String getAlias();

    String getExpression();

    String getKey();

    String getContext();

    boolean isManual();

    boolean isCacheContext();

}
