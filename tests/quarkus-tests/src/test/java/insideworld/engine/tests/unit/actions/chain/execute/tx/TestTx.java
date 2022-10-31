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

package insideworld.engine.tests.unit.actions.chain.execute.tx;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.tests.data.SomeEntity;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

/**
 * Test for transaction execute of child actions.
 * @since 0.14.0
 */
@QuarkusTest
class TestTx {

    /**
     * Class action executor.
     */
    private ActionExecutor<Class<? extends Action>> executor;

    /**
     * Storage for test entities.
     */
    private Storage<SomeEntity> storage;

    /**
     * Object factory.
     */
    private ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     * @param storage Storage for test entities.
     * @param factory Object factory.
     */
    @Inject
    public TestTx(final ActionExecutor<Class<? extends Action>> executor,
                  final Storage<SomeEntity> storage,
                  final ObjectFactory factory) {
        this.executor = executor;
        this.storage = storage;
        this.factory = factory;
    }

    /**
     * TC:
     * Create an entity and execute a parent action with child action in the same TX.
     * In the parent link will update entity value and throw an exception to rollback the TX.
     * ER:
     * The value into entity after repeated read the entity should be the same because TX in
     * executed action rolled back.
     * @throws StorageException Some storage exceptions.
     */
    @Test
    @Transactional
    public void testSameTransaction() throws StorageException {
        final SomeEntity entity = this.createEntity();
        assert this.execute(ParentSameTxAction.class, entity);
        final SomeEntity read = this.storage.read(entity.getId());
        assert read.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * TC:
     * Create an entity and execute a parent action with child action in the new TX.
     * In the parent link will update entity value and throw an exception to rollback the TX.
     * ER:
     * The value into entity after repeated read the entity should be another because the values
     * was changed in new TX inside child action.
     * @throws StorageException Some storage exceptions.
     */
    @Test
    @Transactional
    public void testNewTransaction() throws StorageException {
        final SomeEntity entity = this.createEntity();
        assert this.execute(ParentNewTxAction.class, entity);
        final SomeEntity read = this.storage.read(entity.getId());
        assert !read.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * Create entity and write it in new TX.
     * @return Created and saved entity.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SomeEntity createEntity() {
        final SomeEntity entity = this.factory.createObject(SomeEntity.class);
        final SomeEntity write = this.storage.write(entity);
        write.setValue(String.valueOf(write.getId()));
        return this.storage.write(write);
    }

    /**
     * Common method to execute action.
     * @param action Action to execute.
     * @param entity Test entity.
     * @return Was exception or not.
     */
    private boolean execute(final Class<? extends Action> action, final SomeEntity entity) {
        final Context context = this.executor.createContext();
        context.put(StorageTags.ID, entity.getId());
        boolean exception = false;
        try {
            this.executor.execute(action, context);
        } catch (final CustomException exp) {
            exception = true;
        }
        return exception;
    }
}
