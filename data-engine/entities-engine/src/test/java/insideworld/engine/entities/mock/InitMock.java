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

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.mock.entities.positive.MockEntity;
import insideworld.engine.entities.mock.entities.positive.MockEntityImpl;
import insideworld.engine.entities.mock.entities.positive.MockOneEntity;
import insideworld.engine.entities.mock.entities.positive.MockOneEntityImpl;
import insideworld.engine.entities.mock.entities.positive.MockTwoEntity;
import insideworld.engine.entities.mock.entities.positive.MockTwoEntityImpl;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.injection.ObjectFactory;
import io.quarkus.runtime.Startup;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Init mock storages.
 *
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
     *
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
     *
     * @throws StorageException Some storage exception.
     */
    @PostConstruct
    public final void post() throws StorageException {
        this.createOne();
        this.createTwo();
        this.createPrimary();
        this.createPrimary();
        this.createPrimary();
    }

    /**
     * Write primary.
     *
     * @return New mock entity.
     * @throws StorageException Some exception.
     */
    public final MockEntity createPrimary() throws StorageException {
        final MockEntityImpl entity = this.factory.createObject(MockEntityImpl.class);
        entity.setDate(new Date(1_000_000));
        entity.setDates(
            List.of(new Date(2_000_000), new Date(3_000_000), new Date(4_000_000))
        );
        entity.setPrim(1337);
        entity.setStrprim("7331");
        entity.setWrapprim(1414L);
        entity.setStrprims(List.of("One", "Two"));
        entity.setWrapprims(List.of(1L, 2L, 3L, 4L));
        entity.setOne(this.one.read(1));
        entity.setTwos(
            List.of(this.two.read(1), this.two.read(2), this.two.read(3))
        );
        return this.primary.write(entity);
    }

    /**
     * Fill context by DTO entity.
     * @param context Context to fill.
     * @checkstyle NonStaticMethodCheck (10 lines)
     */
    public final void fillContext(final Context context) {
        context.put(MockTags.STRPRIM, "Value");
        context.put(MockTags.WRAPPRIMS, List.of(5L, 6L));
        context.put(MockTags.ONE_ID, 2L);
        context.put(MockTags.TWOS_IDS, List.of(7L, 8L, 9L));
        context.put(MockTags.DATE, new Date(1_500_000));
        context.put(MockTags.DATES, List.of(new Date(1_500_000), new Date(2_500_000)));
    }

    /**
     * Create one entity.
     *
     * @throws StorageException Some exception.
     */
    private void createOne() throws StorageException {
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 0; idx < 10; idx++) {
            final MockOneEntityImpl entity = this.factory.createObject(MockOneEntityImpl.class);
            entity.setId((long) idx);
            this.one.write(entity);
        }
    }

    /**
     * Create two entity.
     *
     * @throws StorageException Some exception.
     */
    private void createTwo() throws StorageException {
        //@checkstyle IllegalTokenCheck (2 lines)
        for (int idx = 0; idx < 10; idx++) {
            final MockTwoEntityImpl entity = this.factory.createObject(MockTwoEntityImpl.class);
            entity.setId((long) idx);
            this.two.write(entity);
        }
    }
}
