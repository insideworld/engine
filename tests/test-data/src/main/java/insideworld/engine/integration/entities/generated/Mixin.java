package insideworld.engine.integration.entities.generated;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;

@GenerateJpaEntity(entity = GeneratedChildEntity.class, schema = "generated", table = "child")
@GenerateJpaEntity(entity = GeneratedTopEntity.class, schema = "generated", table = "top")
@GenerateCrud(entity = GeneratedChildEntity.class)
@GenerateCrud(entity = GeneratedTopEntity.class)
public interface Mixin extends GenerateMixin {
}
