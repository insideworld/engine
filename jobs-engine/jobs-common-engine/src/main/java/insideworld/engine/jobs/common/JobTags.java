package insideworld.engine.jobs.common;

import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.jobs.common.entity.JobEntity;

public final class JobTags {

    private JobTags() {
    }

    public static final EntityTag<JobEntity> JOB = new EntityTag<>("jobs.entity");

}
