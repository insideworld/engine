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

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.profiles.AbstractExecuteWrapper;
import insideworld.engine.actions.executor.profiles.DefaultExecuteProfile;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.actions.StorageActionsTags;
import insideworld.engine.exception.CommonException;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Singleton;
import javax.transaction.Transactional;

/**
 * Action wrapper to create a transaction.
 * If you need call action in separate transaction -
 * put in context StorageActionsTags.USE_EXIST_TX tag.
 *
 * @since 0.14.0
 */
@Singleton
public class TxActionWrapper extends AbstractExecuteWrapper {

    @Override
    public final void execute(final Action action, final Context context, final Output output)
        throws CommonException {
        if (context.contains(StorageActionsTags.USE_EXIST_TX)) {
            this.executeSameTx(action, context, output);
        } else {
            this.executeNewTx(action, context, output);
        }
    }

    /**
     * Use same transactions.
     *
     * @param action Action.
     * @param context Context.
     * @param output Output.
     * @throws CommonException Common exception.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public final void executeSameTx(final Action action, final Context context, final Output output)
        throws CommonException {
        super.execute(action, context, output);
    }

    /**
     * Create a new transaction.
     *
     * @param action Action.
     * @param context Context.
     * @param output Output.
     * @throws CommonException Common exception.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = Exception.class)
    public final void executeNewTx(final Action action, final Context context, final Output output)
        throws CommonException {
        super.execute(action, context, output);
    }

    @Override
    public final long wrapperOrder() {
        return 1_000_000;
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(DefaultExecuteProfile.class);
    }
}
