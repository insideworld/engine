package insideworld.engine.integration.entities.convertor;

import insideworld.engine.data.generator.jpa.GenerateMixin;
import insideworld.engine.data.generator.jpa.actions.delete.annotations.GenerateDeleteAction;
import insideworld.engine.data.generator.jpa.actions.read.annotations.GenerateReadAction;
import insideworld.engine.data.generator.jpa.actions.write.annotations.GenerateWriteAction;
import insideworld.engine.data.generator.jpa.entity.annotations.GenerateJpaEntity;
import insideworld.engine.data.generator.jpa.storage.annotations.GenerateCrud;
import insideworld.engine.integration.entities.generated.GeneratedChildEntity;
import insideworld.engine.integration.entities.generated.GeneratedTopEntity;

@GenerateJpaEntity(entity = TestOne.class, schema = "entities", table = "one")
@GenerateCrud(entity = TestOne.class)
@GenerateReadAction(entity = TestOne.class, tag = "one", tags = "ones", key = "one.read")
@GenerateWriteAction(entity = TestOne.class, tag = "one", key = "one.write")
@GenerateDeleteAction(entity = TestOne.class, tag = "one", tags = "ones", key = "one.delete")

@GenerateJpaEntity(entity = TestTwo.class, schema = "entities", table = "two")
@GenerateCrud(entity = TestTwo.class)
@GenerateReadAction(entity = TestTwo.class, tag = "two", tags = "twos", key = "two.read")
@GenerateWriteAction(entity = TestTwo.class, tag = "two", key = "two.write")
@GenerateDeleteAction(entity = TestTwo.class, tag = "two", tags = "twos", key = "two.delete")

@GenerateJpaEntity(entity = TestArray.class, schema = "entities", table = "arrays")
@GenerateCrud(entity = TestArray.class)
@GenerateReadAction(entity = TestArray.class, tag = "array", tags = "arrays", key = "array.read")
@GenerateWriteAction(entity = TestArray.class, tag = "array", key = "array.write")
@GenerateDeleteAction(entity = TestArray.class, tag = "array", tags = "arrays", key = "array.delete")

@GenerateJpaEntity(entity = TestMain.class, schema = "entities", table = "main")
@GenerateCrud(entity = TestMain.class)
@GenerateReadAction(entity = TestMain.class, tag = "main", tags = "mains", key = "main.read")
@GenerateWriteAction(entity = TestMain.class, tag = "main", key = "main.write")
@GenerateDeleteAction(entity = TestMain.class, tag = "main", tags = "mains", key = "main.delete")
public interface Mixin extends GenerateMixin {
}
