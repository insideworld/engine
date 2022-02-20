package insideworld.engine.integration.entities.generated;


import insideworld.engine.data.generator.inmemory.entity.annotations.GenerateInMemoryEntity;
import insideworld.engine.data.generator.inmemory.storage.annotations.GenerateInMemoryCrud;
import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.entities.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.generator.entities.actions.read.annotations.GenerateReadAction;
import insideworld.engine.generator.entities.actions.write.annotations.GenerateWriteAction;


@GenerateJpaEntity(entity = GeneratedChildEntity.class, schema = "generated", table = "child")
@GenerateJpaEntity(entity = GeneratedTopEntity.class, schema = "generated", table = "top")
@GenerateCrud(entity = GeneratedChildEntity.class)
@GenerateCrud(entity = GeneratedTopEntity.class)
@GenerateReadAction(entity = GeneratedChildEntity.class, tag = "child", tags = "childs", key = "child.read")
@GenerateReadAction(entity = GeneratedTopEntity.class, tag = "top", tags = "tops", key = "top.read")
@GenerateWriteAction(entity = GeneratedChildEntity.class, tag = "child", key = "child.write")
@GenerateWriteAction(entity = GeneratedTopEntity.class, tag = "top", key = "top.write")
@GenerateDeleteAction(entity = GeneratedChildEntity.class, tag = "child", tags = "childs", key = "child.delete")
@GenerateDeleteAction(entity = GeneratedTopEntity.class, tag = "top", tags = "tops", key = "top.delete")

@GenerateInMemoryEntity(entity = GeneratedInMemoryEntity.class)
@GenerateInMemoryCrud(entity = GeneratedInMemoryEntity.class)

public interface Mixin extends GenerateMixin {
}
