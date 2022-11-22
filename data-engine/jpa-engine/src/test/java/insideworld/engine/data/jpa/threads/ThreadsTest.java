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

package insideworld.engine.data.jpa.threads;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.data.jpa.entities.SomeEntity;
import insideworld.engine.data.jpa.entities.TestTags;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.actions.StorageActionsTags;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.matchers.exception.ExceptionCatchMatcher;
import insideworld.engine.matchers.exception.ExceptionMatchers;
import insideworld.engine.threads.Task;
import insideworld.engine.threads.TaskBuilder;
import insideworld.engine.threads.TaskException;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@QuarkusTest
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
    final void test() throws StorageException, TaskException {
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
        final Collection<Throwable> exceptions = Lists.newLinkedList();
        final Task<Output> task = builder
            .add(() -> this.executor.execute(WriteEntityAction.class, onectx))
            .add(() -> this.executor.execute(WriteEntityAction.class, twoctx))
            .add(() -> this.executor.execute(WriteEntityAction.class, expctx))
            .combine(outputs -> null, Output.class)
            .exception(exp -> {
                exceptions.add(exp);
                return null;
            })
            .build();
        task.result();
        MatcherAssert.assertThat(
            "Should be an exception",
            task::result,
            ExceptionMatchers.catchException(TaskException.class)
        );
        System.out.println("qwe");

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
