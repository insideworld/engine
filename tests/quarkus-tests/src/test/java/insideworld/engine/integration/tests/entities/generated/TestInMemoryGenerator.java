/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
