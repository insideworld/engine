package insideworld.engine.integration.entities.generated;


import insideworld.engine.data.generator.jpa.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.annotations.GeneratesJpa;

@GenerateJpaEntity(entity = GeneratedChildEntity.class, schema = "generated", table = "child")
@GenerateJpaEntity(entity = GeneratedTopEntity.class, schema = "generated", table = "top")
public interface GenerateEntity extends GeneratesJpa {
}
