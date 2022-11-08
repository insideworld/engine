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

package insideworld.engine.entities.mock;

import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.mock.entities.positive.MockEntityImpl;
import insideworld.engine.entities.mock.entities.positive.MockOneEntity;
import insideworld.engine.entities.mock.entities.positive.MockOneEntityImpl;
import insideworld.engine.entities.mock.entities.positive.MockTwoEntity;
import insideworld.engine.entities.mock.entities.positive.MockTwoEntityImpl;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.StorageException;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Init mock storages.
 * @since 0.14.0
 */
@Startup(3000)
@Singleton
public class InitMock {

    /**
     * Primary storage.
     */
    private final Storage<MockEntity> primary;

    /**
     * One storage.
     */
    private final Storage<MockOneEntity> one;

    /**
     * Two storage.
     */
    private final Storage<MockTwoEntity> two;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     * @param primary Primary storage.
     * @param one One storage.
     * @param two Two storage.
     * @param factory Object factory.
     * @checkstyle ParameterNumberCheck (10 lines)
     */
    @Inject
    public InitMock(
        final Storage<MockEntity> primary,
        final Storage<MockOneEntity> one,
        final Storage<MockTwoEntity> two,
        final ObjectFactory factory) {
        this.primary = primary;
        this.one = one;
        this.two = two;
        this.factory = factory;
    }

    /**
     * Post construct method.
     * @throws StorageException Some storage exception.
     */
    @PostConstruct
    public final void post() throws StorageException {
        this.createOne();
        this.createTwo();
        this.createPrimary();
    }

    /**
     * Write primary.
     * @return New mock entity.
     * @throws StorageException Some exception.
     */
    public final MockEntity createPrimary() throws StorageException {
        final MockEntityImpl entity = this.factory.createObject(MockEntityImpl.class);
        entity.setDate(new Date(1_000_000));
        entity.setDates(
            List.of(new Date(2_000_000), new Date(3_000_000), new Date(4_000_000))
        );
        entity.setValue("Some value");
        entity.setValues(List.of(1L, 2L, 3L, 4L, 5L));
        entity.setOne(this.one.read(1));
        entity.setTwos(
            List.of(this.two.read(1), this.two.read(2), this.two.read(3))
        );
        return this.primary.write(entity);
    }

    /**
     * Create one entity.
     */
    private void createOne() {
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 0; idx < 10; idx++) {
            final MockOneEntityImpl entity = this.factory.createObject(MockOneEntityImpl.class);
            entity.setId((long) idx);
            this.one.write(entity);
        }
    }

    /**
     * Create two entity.
     */
    private void createTwo() {
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 0; idx < 10; idx++) {
            final MockTwoEntityImpl entity = this.factory.createObject(MockTwoEntityImpl.class);
            entity.setId((long) idx);
            this.two.write(entity);
        }
    }
}
