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

package insideworld.engine.core.data.jpa.transaction;

import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.action.executor.ActionExecutor;
import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.key.ClassKey;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.core.common.matchers.exception.ExceptionMatchers;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.action.TransactionTags;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import insideworld.engine.frameworks.quarkus.test.database.DatabaseResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

/**
 * Test action transaction wrapper.
 *
 * @since 0.14.0
 */
@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
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
     * Execute new action in the same transaction.
     * In executeNewTx throw exception to rollback TX.
     * ER:
     * Modification of an entity in database should absent.
     *
     * @throws StorageException Some storage exception.
     */
    @Test
    final void testExistTx() throws StorageException {
        final SomeEntity entity = this.execute(
            context -> context.put(TransactionTags.USE_SAME_TX, true)
        );
        assert entity.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * TC:
     * Execute new action in a new transaction.
     * In executeNewTx throw exception to rollback TX where action was executed.
     * ER:
     * Modification of an entity in database should be.
     *
     * @throws StorageException Shouldn't raise.
     */
    @Test
    final void testNewTx() throws StorageException {
        final SomeEntity entity = this.execute(null);
        assert !entity.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * Execute a new action in new TX and through an exception.
     *
     * @param context Context with filled entity and additional tags.
     * @throws ActionException Shouldn't raise.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    final void executeNewTx(
        final SomeEntity entity,
        final Consumer<ExecuteContext> context
    ) throws CommonException {
        this.executor.execute(new ClassKey<>(TestActionTX.class), entity, context);
        throw new CustomException();
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
     * Create a new entity and catch necessary exception.
     *
     * @param context Context with filled additional tags.
     * @return Repeated read entity.
     * @throws StorageException Some storage exception.
     */
    private SomeEntity execute(final Consumer<ExecuteContext> context) throws StorageException {
        final SomeEntity entity = this.createEntity();
        MatcherAssert.assertThat(
            "Exception expected.",
            () -> this.executeNewTx(entity, context),
            ExceptionMatchers.catchException(CustomException.class)
        );
        return this.storage.read(entity.getId());
    }
}
