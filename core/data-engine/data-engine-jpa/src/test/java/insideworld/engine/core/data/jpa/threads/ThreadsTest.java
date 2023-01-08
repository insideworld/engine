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

package insideworld.engine.core.data.jpa.threads;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.executor.old.ActionExecutor;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.data.jpa.entities.TestTags;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.common.injection.ObjectFactory;
import insideworld.engine.frameworks.quarkus.test.database.DatabaseResource;
import insideworld.engine.core.common.threads.Task;
import insideworld.engine.core.common.threads.TaskBuilder;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(DatabaseResource.class)
class ThreadsTest {

    private final ActionExecutor<Class<? extends Action>> executor;
    private final ObjectFactory factory;
    private final Storage<SomeEntity> storage;

    @Inject
    ThreadsTest(
        final ActionExecutor<Class<? extends Action>> executor,
        final ObjectFactory factory,
        final Storage<SomeEntity> storage
    ) {
        this.executor = executor;
        this.factory = factory;
        this.storage = storage;
    }

    @Test
    @Transactional
    final void test() throws StorageException {
        final TaskBuilder<Output, Output> builder =
            this.factory.createObject(new TypeLiteral<>() {
            });
        final Context onectx = this.executor.createContext();
        final SomeEntity oneent = this.createEntity();
        onectx.put(TestTags.SOME_ENTITY, oneent);
        onectx.put(ThreadsTag.ANOTHER_EXCEPTION, new Object());
        final Context twoctx = this.executor.createContext();
        final SomeEntity twoent = this.createEntity();
        twoctx.put(TestTags.SOME_ENTITY, twoent);
        final Context expctx = this.executor.createContext();
        final SomeEntity expent = this.createEntity();
        expctx.put(TestTags.SOME_ENTITY, expent);
        expctx.put(ThreadsTag.EXCEPTION, new Object());
        final Task<Output> task = builder
            .add(() -> this.executor.execute(WriteEntityAction.class, onectx))
            .add(() -> this.executor.execute(WriteEntityAction.class, twoctx))
            .add(() -> this.executor.execute(WriteEntityAction.class, expctx))
            .combine(outputs -> null, Output.class)
            .exception(exp -> null)
            .build();
        task.result();
        MatcherAssert.assertThat(
            "Should be an exception in list",
            task.exceptions(),
            Matchers.iterableWithSize(2)
        );
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

}
