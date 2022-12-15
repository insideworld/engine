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
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import insideworld.engine.core.data.jpa.entities.TestTags;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.common.exception.CommonException;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WriteEntityAction implements Action {

    private final Storage<SomeEntity> storage;

    @Inject
    public WriteEntityAction(final Storage<SomeEntity> storage) {
        this.storage = storage;
    }

    @Override
    public void execute(final Context context, final Output output) throws CommonException {
        final SomeEntity entity = context.get(TestTags.SOME_ENTITY);
        entity.setValue("NewValueForTestThreads");
        this.storage.write(entity);
        if (context.contains(ThreadsTag.EXCEPTION)) {
            throw new IllegalArgumentException("Exception for test threads");
        }
        if (context.contains(ThreadsTag.ANOTHER_EXCEPTION)) {
            throw new TestException("Another");
        }
    }

    @Override
    public String key() {
        return "insideworld.engine.data.jpa.threads.WriteEntityAction";
    }
}
