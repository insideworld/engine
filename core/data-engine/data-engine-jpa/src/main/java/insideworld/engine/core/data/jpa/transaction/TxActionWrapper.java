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

import insideworld.engine.core.action.executor.ExecuteContext;
import insideworld.engine.core.action.executor.profile.DefaultExecuteProfile;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.action.executor.profile.wrapper.ExecuteWrapper;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.data.core.action.TransactionTags;
import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import javax.enterprise.context.Dependent;
import javax.transaction.Transactional;

/**
 * Action wrapper to create a transaction.
 * If you need call action in separate transaction -
 * put in context StorageActionsTags.USE_EXIST_TX tag.
 *
 * @since 0.14.0
 */
@Dependent
public class TxActionWrapper implements ExecuteWrapper {

    @Override
    public final void execute(final ExecuteContext context, final Queue<ExecuteWrapper> wrappers)
        throws CommonException {
        if (Boolean.TRUE.equals(context.get(TransactionTags.USE_SAME_TX))) {
            this.sameTx(context, wrappers);
        } else {
            this.newTx(context, wrappers);
        }
    }

    /**
     * Use same transactions.
     *
     * @param context Wrapper context.
     * @throws CommonException Common exception.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = Exception.class)
    public final void sameTx(final ExecuteContext context, final Queue<ExecuteWrapper> wrappers)
        throws CommonException {
        this.next(context, wrappers);
    }

    /**
     * Create a new transaction.
     *
     * @param context Wrapper context.
     * @throws CommonException Common exception.
     * @checkstyle NonStaticMethodCheck (2 lines)
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW, rollbackOn = Exception.class)
    public final void newTx(final ExecuteContext context, final Queue<ExecuteWrapper> wrappers)
        throws CommonException {
        this.next(context, wrappers);
    }

    @Override
    public final long wrapperOrder() {
        return 1_000_000;
    }

    @Override
    public Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(DefaultExecuteProfile.class);
    }
}
