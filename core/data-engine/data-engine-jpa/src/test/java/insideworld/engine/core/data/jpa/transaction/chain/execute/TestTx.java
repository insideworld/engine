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

package insideworld.engine.core.data.jpa.transaction.chain.execute;

import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

/**
 * Test for transaction execute of child actions.
 *
 * @since 0.14.0
 */
@QuarkusTest
class TestTx {

    /**
     * Class action executor.
     */
    private final ActionExecutor executor;

    /**
     * Storage for test entities.
     */
    private final Storage<SomeEntity> storage;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Default constructor.
     *
     * @param executor Class action executor.
     * @param storage Storage for test entities.
     * @param factory Object factory.
     */
    @Inject
    TestTx(
        final ActionExecutor executor,
        final Storage<SomeEntity> storage,
        final ObjectFactory factory
    ) {
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
     *
     * @throws StorageException Some storage exceptions.
     */
    @Test
    @Transactional
    final void testSameTransaction() throws StorageException {
        final SomeEntity entity = this.createEntity();
        assert this.execute(new ClassKey<>(ParentSameTxAction.class), entity);
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
     *
     * @throws StorageException Some storage exceptions.
     */
    @Test
    @Transactional
    final void testNewTransaction() throws StorageException {
        final SomeEntity entity = this.createEntity();
        assert this.execute(new ClassKey<>(ParentNewTxAction.class), entity);
        final SomeEntity read = this.storage.read(entity.getId());
        assert !read.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * Create entity and write it in new TX.
     *
     * @return Created and saved entity.
     * @throws StorageException Shouldn't raise.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    final SomeEntity createEntity() throws StorageException {
        final SomeEntity entity = this.factory.createObject(SomeEntity.class);
        final SomeEntity write = this.storage.write(entity);
        write.setValue(String.valueOf(write.getId()));
        return this.storage.write(write);
    }

    /**
     * Common method to execute action.
     *
     * @param action Action to execute.
     * @param entity Test entity.
     * @return Was exception or not.
     */
    private boolean execute(final Key<Long, SomeEntity> action, final SomeEntity entity) {
        boolean exception = false;
        try {
            this.executor.execute(action, entity.getId());
        } catch (final CommonException exp) {
            exception = true;
        }
        return exception;
    }
}
