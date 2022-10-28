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
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.integration.transactions.SomeEntity;
import insideworld.engine.integration.transactions.TxTags;
import javax.inject.Singleton;

/**
 * Change a value on the entity to another.
 * @since 0.14.0
 */
@Singleton
class ChildAction implements Action {

    /**
     * Test entity storage.
     */
    private final Storage<SomeEntity> storage;

    /**
     * Default constructor.
     * @param storage Test entity storage.
     */
    public ChildAction(final Storage<SomeEntity> storage) {
        this.storage = storage;
    }

    @Override
    public final void execute(final Context context, final Output output) throws ActionException {
        final SomeEntity entity = context.get(TxTags.SOME_ENTITY);
        entity.setValue("AnotherValue");
        this.storage.write(entity);
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.chain.execute.tx.ChildAction";
    }
}
