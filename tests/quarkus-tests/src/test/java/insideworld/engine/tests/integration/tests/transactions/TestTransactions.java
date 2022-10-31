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

package insideworld.engine.tests.integration.tests.transactions;

import insideworld.engine.actions.executor.impl.ClassActionExecutor;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.tests.data.CrudSomeEntity;
import insideworld.engine.tests.data.SomeEntity;
import insideworld.engine.tests.data.SomeNestedEntity;
import insideworld.engine.tests.data.SomeNestedEntityJPA;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TestTransactions {

    private final CrudSomeEntity some;
    private final Storage<SomeNestedEntity> nested;
    private ClassActionExecutor executor;

    @Inject
    public TestTransactions(final CrudSomeEntity some,
                            final Storage<SomeNestedEntity> nested,
                            final ClassActionExecutor executor) {
        this.some = some;
        this.nested = nested;
        this.executor = executor;
    }


    @Test

    public void test() throws StorageException {
//        final SomeEntity read = some.read(0);
//
        try {
            this.write();
        } catch (Exception qwe) {

        }

      this.read();
        System.out.println("qwe");
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void write() {
        for (SomeEntity read : some.findByQuery()) {
            final Context context = this.executor.createContext();
//            final SomeNestedEntity nestedent = read.getNestedEntity();
            final SomeNestedEntity nestedent = new SomeNestedEntityJPA();
            nestedent.setOne(12L);
            nestedent.setTwo(45L);
//            PanacheRepository qwe = (PanacheRepository) nested;
//            final EntityManager entityManager = qwe.getEntityManager();
//            entityManager.detach(nestedent);
            nestedent.setTwo(5L);
            context.put("nested", nestedent);
            this.executor.executeSameTx(NewTxAction.class, context, SystemExecuteProfile.class);
//            throw new RuntimeException();
//            System.out.println("qwe");
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void read() {
        for (SomeEntity read : some.findByQuery()) {
            System.out.println("qwe");
        }
    }
}
