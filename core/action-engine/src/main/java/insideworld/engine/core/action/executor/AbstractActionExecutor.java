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

package insideworld.engine.core.action.executor;

import insideworld.engine.core.action.Action;
import insideworld.engine.core.action.ActionException;
import insideworld.engine.core.common.exception.CommonException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract action executor which provide a logic to execute an action.
 *
 * @param <T>
 */
public abstract class AbstractActionExecutor<T> implements ActionChanger {

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<T, Action<?, ?>> actions = new ConcurrentHashMap<>(16);

    public final <I, O> O execute(T key, I input) throws CommonException {
        if (this.lock.isLocked()) {
            //TODO: Add normal exception. It's just a stub and will fail with NPE.
            throw new ActionException(null, "Actions under update");
        }
        @SuppressWarnings("unchecked") final Action<I, O> action = (Action<I, O>) this.actions.get(key);
        try {
            return action.execute(input);
        } catch (final Exception exp) {
            throw CommonException.wrap(
                exp,
                () -> new ActionException(action, exp),
                ActionException.class
            );
        }
    }

    protected abstract T calculateKey(Action<?, ?> action);

    @Override
    public final void addAction(final Action<?, ?> action) {
        this.lock.lock();
        this.actions.put(this.calculateKey(action), action);
        this.lock.unlock();
    }

}
