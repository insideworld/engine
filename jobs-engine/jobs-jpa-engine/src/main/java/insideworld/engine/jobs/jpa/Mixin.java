package insideworld.engine.jobs.jpa;

import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.jobs.common.entity.JobEntity;

@GenerateJpaEntity(entity = JobEntity.class, table = "jobs", schema = "jobs")
@GenerateCrud(entity = JobEntity.class)
public interface Mixin extends GenerateMixin {
}
