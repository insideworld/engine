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

package insideworld.engine.data.jpa.transaction;

import insideworld.engine.actions.chain.execute.PreExecute;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.actions.StorageActionsTags;
import javax.inject.Singleton;

/**
 * Use same TX pre executor.
 * Using to ExecuteActionLink for execute action in the same TX.
 * @since 0.14.0
 */
@Singleton
public class UseSameTx implements PreExecute {

    /**
     * Dummy object for tag.
     */
    private static final Object DUMMY = new Object();

    @Override
    public final boolean apply(final Context parent, final Context child) {
        child.put(StorageActionsTags.USE_EXIST_TX, UseSameTx.DUMMY);
        return true;
    }
}
