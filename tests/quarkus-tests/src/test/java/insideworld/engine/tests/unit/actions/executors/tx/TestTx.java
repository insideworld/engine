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

package insideworld.engine.tests.unit.actions.executors.tx;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.integration.transactions.SomeEntity;
import insideworld.engine.integration.transactions.TxTags;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

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
     * Execute new action in the same transaction.
     * In executeNewTx throw exception to rollback TX.
     * ER:
     * Modification of an entity in database should absent.
     * @throws StorageException Some storage exception.
     */
    @Test
    public final void testExistTx() throws StorageException {
        final Context context = this.executor.createContext();
        context.put(ActionsTags.USE_EXIST_TX, true);
        final SomeEntity entity = this.execute(context);
        assert entity.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * TC:
     * Execute new action in a new transaction.
     * In executeNewTx throw exception to rollback TX where action was executed.
     * ER:
     * Modification of an entity in database should be.
     * @throws StorageException Some storage exception.
     */
    @Test
    public final void testNewTx() throws StorageException {
        final Context context = this.executor.createContext();
        final SomeEntity entity = this.execute(context);
        assert !entity.getValue().equals(String.valueOf(entity.getId()));
    }

    /**
     * Execute a new action in new TX and through an exception.
     * @param context Context with filled entity and additional tags.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public final void executeNewTx(final Context context) {
        this.executor.execute(TestActionTX.class, context);
        throw new CustomException();
    }

    /**
     * Create entity and write it in new TX.
     * @return Created and saved entity.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public final SomeEntity createEntity() {
        final SomeEntity entity = this.factory.createObject(SomeEntity.class);
        final SomeEntity write = this.storage.write(entity);
        write.setValue(String.valueOf(write.getId()));
        return this.storage.write(write);
    }

    /**
     * Create a new entity and catch necessary exception.
     * @param context Context with filled additional tags.
     * @return Repeated read entity.
     * @throws StorageException Some storage exception.
     */
    private SomeEntity execute(final Context context) throws StorageException {
        final SomeEntity entity = this.createEntity();
        context.put(TxTags.SOME_ENTITY, entity);
        boolean wasexp = false;
        try {
            this.executeNewTx(context);
        } catch (final CustomException exp) {
            wasexp = true;
        }
        assert wasexp;
        return this.storage.read(entity.getId());
    }
}
