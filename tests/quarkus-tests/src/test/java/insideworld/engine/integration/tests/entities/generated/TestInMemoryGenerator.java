package insideworld.engine.integration.tests.entities.generated;

import insideworld.engine.data.generator.inmemory.entity.abstracts.MemoryEntity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.entities.generated.GeneratedInMemoryEntity;
import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestInMemoryGenerator {

    private final ObjectFactory factory;
    private final Storage<GeneratedInMemoryEntity> storage;

    @Inject
    public TestInMemoryGenerator(final ObjectFactory factory,
                                 final Storage<GeneratedInMemoryEntity> storage) {
        this.factory = factory;
        this.storage = storage;
    }

    @Test
    public void test() throws StorageException {
        this.storage.write(this.createEntity(1337));
        this.storage.write(this.createEntity(1));
        this.storage.write(this.createEntity(2));
        assert this.storage.readAll().size() == 3;
        assert this.storage.read(1337).getSome().equals("Test some 1337");
    }


    private GeneratedInMemoryEntity createEntity(int id) {
        final GeneratedInMemoryEntity entity = this.factory.createObject(GeneratedInMemoryEntity.class);
        final MemoryEntity memory = (MemoryEntity) entity;
        memory.setId(id);
        entity.setMessage("Test message " + id);
        entity.setSome("Test some " + id);
        return entity;
    }

}
