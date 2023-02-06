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
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WriteEntityAction implements Action<WriteEntityAction.Input, Void> {

    private final Storage<SomeEntity> storage;

    @Inject
    public WriteEntityAction(final Storage<SomeEntity> storage) {
        this.storage = storage;
    }

    @Override
    public Void execute(final Input input) throws CommonException {
        final SomeEntity entity = input.getEntity();
        entity.setValue("NewValueForTestThreads");
        this.storage.write(entity);
        if (Integer.valueOf(1).equals(input.getException())) {
            throw new IllegalArgumentException("Exception for test threads");
        }
        if (Integer.valueOf(2).equals(input.getException())) {
            throw new TestException("Another");
        }
        return null;
    }

    @Override
    public String key() {
        return "insideworld.engine.data.jpa.threads.WriteEntityAction";
    }

    @Override
    public final void types(final Input input, final Void output) {
        //Nothing to do
    }

    public interface Input {

        SomeEntity getEntity();

        Integer getException();

    }
}
