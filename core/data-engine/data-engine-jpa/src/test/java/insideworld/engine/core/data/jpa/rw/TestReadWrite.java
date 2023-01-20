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

package insideworld.engine.core.data.jpa.rw;

import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.jpa.rw.entities.ManyToOneEntity;
import insideworld.engine.core.data.jpa.rw.entities.OneToManyEntity;
import insideworld.engine.core.data.jpa.rw.entities.OneToManyEntityImpl;
import insideworld.engine.core.data.jpa.rw.entities.OneToOneEntity;
import insideworld.engine.core.data.jpa.rw.entities.OneToOneEntityImpl;
import insideworld.engine.core.data.jpa.rw.entities.PrimaryEntity;
import insideworld.engine.core.data.jpa.rw.entities.PrimaryEntityImpl;
import insideworld.engine.frameworks.quarkus.test.database.DatabaseResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
class TestReadWrite {


    private final Storage<PrimaryEntity> strprimary;
    private final Storage<OneToOneEntity> stronetoone;
    private final Storage<OneToManyEntity> stronetomany;
    private final Storage<ManyToOneEntity> strmanytoone;

    @Inject
    TestReadWrite(
        final Storage<PrimaryEntity> strprimary,
        final Storage<OneToOneEntity> stronetoone,
        final Storage<OneToManyEntity> stronetomany,
        final Storage<ManyToOneEntity> strmanytoone
    ) {
        this.strprimary = strprimary;
        this.stronetoone = stronetoone;
        this.stronetomany = stronetomany;
        this.strmanytoone = strmanytoone;
    }

    @Test
    public void test() throws StorageException {
        final PrimaryEntity primary = this.createPrimary();
        final OneToOneEntityImpl first = new OneToOneEntityImpl();
        first.setPrimary(primary);
        first.setValue("qwe");
        this.writeOneToOne(first);
        OneToOneEntity read = this.stronetoone.read(primary.getId());
        final OneToOneEntityImpl second = new OneToOneEntityImpl();
        second.setId(primary.getId());
        second.setPrimary(primary);
        second.setValue("ewq");
        this.writeOneToOne(second);
        read = this.stronetoone.read(primary.getId());
        System.out.println();
    }

    @Transactional
    public PrimaryEntity createPrimary() throws StorageException {
        final PrimaryEntityImpl primary = new PrimaryEntityImpl();
        primary.setValue("testValue");
        return strprimary.write(primary);
    }

    @Transactional
    public void writeOneToOne(final OneToOneEntity entity) throws StorageException {
        entity.setPrimary(this.strprimary.merge(entity.getPrimary()));
        if (entity.getId() != 0) {
            final OneToOneEntity merge = this.stronetoone.merge(entity);
            this.stronetoone.write(merge);
            return;
        }
        this.stronetoone.write(entity);
    }

}
