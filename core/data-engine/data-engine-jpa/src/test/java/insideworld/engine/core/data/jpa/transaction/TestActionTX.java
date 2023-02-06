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

import insideworld.engine.core.action.Action;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.jpa.entities.SomeEntity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Test TX on action.
 * Just modify an entity and write it.
 * @since 0.14.0
 */
@Singleton
class TestActionTX implements Action<SomeEntity, SomeEntity> {

    /**
     * Storage for test entity.
     */
    private final Storage<SomeEntity> storage;

    /**
     * Default constructor.
     * @param storage Storage for test entity.
     */
    @Inject
    TestActionTX(final Storage<SomeEntity> storage) {
        this.storage = storage;
    }

    @Override
    public SomeEntity execute(final SomeEntity input) throws CommonException {
        input.setValue("SomeValue");
        return this.storage.write(input);
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.executors.tx.TestActionTX";
    }

    @Override
    public void types(final SomeEntity input, final SomeEntity output) {
        //Nothing to do
    }
}
