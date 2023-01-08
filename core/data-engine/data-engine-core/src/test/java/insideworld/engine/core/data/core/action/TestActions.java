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

package insideworld.engine.core.data.core.action;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.InitMock;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntityImpl;
import insideworld.engine.core.data.core.storages.Storage;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test read, write and delete actions.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestActions {

    /**
     * Class action executor.
     */
    private final ActionExecutor executor;

    /**
     * Init.
     */
    private final InitMock init;

    /**
     * Storage.
     */
    private final Storage<MockEntity> storage;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     * @param init Init.
     * @param storage Storage.
     */
    @Inject
    TestActions(
        final ActionExecutor executor,
        final InitMock init,
        final Storage<MockEntity> storage
    ) {
        this.executor = executor;
        this.init = init;
        this.storage = storage;
    }

    /**
     * TC:
     * Execute read action with id and ids tags.
     * ER:
     * Should put in output DTO entities.
     *
     * @throws StorageException Shouldn't raise.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testRead() throws CommonException {
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final MockEntity[] result = this.executor.execute(
            new ClassKey<>(ReadMockAction.class),
            entities.stream().map(Entity::getId).toArray(Long[]::new)
        );
        MatcherAssert.assertThat(
            "Entity didn't read",
            result,
            Matchers.arrayWithSize(2)
        );
    }

    /**
     * TC: Create entity and entities and try to delete it.
     * ER: Entities should delete.
     *
     * @throws StorageException Shouldn't raise.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testDelete() throws CommonException {
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final int size = this.storage.readAll().size();
        this.executor.execute(
            new ClassKey<>(DeleteMockAction.class),
            entities.stream().map(MockEntity::getId).toArray(Long[]::new)
        );
        MatcherAssert.assertThat(
            "Entity didn't delete",
            this.storage.readAll(),
            Matchers.iterableWithSize(size - entities.size())
        );
    }

    /**
     * TC: Create DTO entity and try to write it.
     * ER: Should present entity with ID in context.
     *
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testWrite() throws CommonException {
        final MockEntityImpl entity = new MockEntityImpl();
        entity.setDate(new Date());
        final MockEntity[] write = this.executor.execute(
            new ClassKey<>(WriteMockAction.class),
            new MockEntity[]{entity}
        );
        MatcherAssert.assertThat(
            "Entity didn't write",
            write[0],
            Matchers.hasProperty(
                "id",
                Matchers.not(
                    Matchers.anyOf(
                        Matchers.is(0L),
                        Matchers.nullValue()
                    )
                )
            )
        );
    }

}
