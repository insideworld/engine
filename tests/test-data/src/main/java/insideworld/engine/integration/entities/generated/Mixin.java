package insideworld.engine.integration.entities.generated;


import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.generator.GenerateMixin;
import insideworld.engine.generator.actions.entities.delete.annotations.GenerateDeleteAction;
import insideworld.engine.generator.actions.entities.read.annotations.GenerateReadAction;
import insideworld.engine.generator.actions.entities.write.annotations.GenerateWriteAction;

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
public interface Mixin extends GenerateMixin {
}
