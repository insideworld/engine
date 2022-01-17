package insideworld.engine.integration.entities.generated;

import insideworld.engine.data.generator.jpa.storage.CrudGenerator;
import insideworld.engine.data.generator.jpa.storage.GenerateCrud;

@GenerateCrud(entity = GeneratedChildEntity.class)
@GenerateCrud(entity = GeneratedTopEntity.class)
public interface GenerateStorage extends CrudGenerator {
}
