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

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.test.KeeperMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.mock.InitMock;
import insideworld.engine.core.data.core.mock.MockTags;
import insideworld.engine.core.data.core.mock.entities.positive.MockEntity;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.core.tags.StorageTags;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test read, write and delete actions.
 * @since 0.14.0
 */
@QuarkusTest
class TestActions {

    /**
     * Class action executor.
     */
    private final ActionExecutor<Class<? extends Action>> executor;

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
     * @param executor Class action executor.
     * @param init Init.
     * @param storage Storage.
     */
    @Inject
    TestActions(
        final ActionExecutor<Class<? extends Action>> executor,
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
     * @throws StorageException Shouldn't raise.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testRead() throws StorageException, ActionException {
        final MockEntity entity = this.init.createPrimary();
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final Context context = this.executor.createContext();
        context.put(StorageTags.ID, entity.getId());
        context.put(StorageTags.IDS, entities.stream().map(MockEntity::getId).toList());
        MatcherAssert.assertThat(
            "Entity didn't read",
            this.executor.execute(ReadMockAction.class, context),
            Matchers.allOf(
                Matchers.iterableWithSize(3),
                Matchers.hasItem(
                    Matchers.allOf(
                        KeeperMatchers.match(StorageTags.ID, Matchers.is(entity.getId())),
                        KeeperMatchers.match(MockTags.STRPRIM, Matchers.is("7331"))
                    )
                )
            )
        );
    }

    /**
     * TC: Create entity and entities and try to delete it.
     * ER: Entities should delete.
     * @throws StorageException Shouldn't raise.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testDelete() throws StorageException, ActionException {
        final MockEntity entity = this.init.createPrimary();
        final Collection<MockEntity> entities =
            List.of(this.init.createPrimary(), this.init.createPrimary());
        final Context context = this.executor.createContext();
        final int size = this.storage.readAll().size();
        context.put(StorageTags.ID, entity.getId());
        context.put(StorageTags.IDS, entities.stream().map(MockEntity::getId).toList());
        this.executor.execute(DeleteMockAction.class, context);
        MatcherAssert.assertThat(
            "Entity didn't delete",
            this.storage.readAll(),
            Matchers.iterableWithSize(size - entities.size() - 1)
        );
    }

    /**
     * TC: Create DTO entity and try to write it.
     * ER: Should present entity with ID in context.
     * @throws ActionException Shouldn't raise.
     */
    @Test
    final void testWrite() throws ActionException {
        final Context context = this.executor.createContext();
        this.init.fillContext(context);
        this.executor.execute(WriteMockAction.class, context);
        MatcherAssert.assertThat(
            "Entity didn't write",
            context,
            KeeperMatchers.match(
                MockTags.PRIMARY,
                Matchers.hasProperty(
                    "id",
                    Matchers.not(
                        Matchers.anyOf(
                            Matchers.is(0L),
                            Matchers.nullValue()
                        )
                    )
                )
            )
        );
    }

}
