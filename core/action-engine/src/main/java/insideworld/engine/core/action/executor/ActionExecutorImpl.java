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
import insideworld.engine.core.action.executor.key.Key;
import insideworld.engine.core.action.executor.key.KeyComputer;
import insideworld.engine.core.action.executor.profile.DefaultExecuteProfile;
import insideworld.engine.core.action.executor.profile.ExecuteProfile;
import insideworld.engine.core.common.exception.CommonException;
import insideworld.engine.core.common.injection.ObjectFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActionExecutorImpl implements ActionExecutor, ActionChanger {

    private final ReentrantLock lock = new ReentrantLock();

    private final Map<Key<?, ?>, Action<?, ?>> actions;

    private final ObjectFactory factory;

    /**
     * All ExecuteProfiles in the system.
     */
    private final Map<Class<? extends ExecuteProfile>, ExecuteProfile> profiles;
    private final List<KeyComputer> computers;

    @Inject
    public ActionExecutorImpl(
        final ObjectFactory factory,
        final List<ExecuteProfile> profiles,
        final List<KeyComputer> computers
    ) {
        this.factory = factory;
        this.profiles = profiles.stream().collect(
            Collectors.toMap(ExecuteProfile::getClass, Function.identity())
        );
        this.computers = computers;
        this.actions = new ConcurrentHashMap<>(16);
    }


    @Override
    public <I, O> O execute(final Key<I, O> key, final I input) throws CommonException {
        return this.execute(
            key,
            input,
            null
        );
    }

    @Override
    public <I, O> O execute(
        final Key<I, O> key,
        final I input,
        final Consumer<ExecuteContext> predicate
    ) throws CommonException {
        if (this.lock.isLocked()) {
            //TODO: Add normal exception. It's just a stub and will fail with NPE.
            throw new ActionException(null, "Actions under update");
        }
        final ExecuteContext context = this.factory.createObject(ExecuteContext.class);
        final Action<?, ?> action = this.actions.get(key);
        if (predicate != null) {
            predicate.accept(context);
        }
        context.put(ExecutorTags.ACTION, action);
        context.put(ExecutorTags.INPUT, input);
        if (!context.contains(ExecutorTags.PROFILE)) {
            context.put(ExecutorTags.PROFILE, DefaultExecuteProfile.class);
        }
        try {
            this.profiles.get(context.get(ExecutorTags.PROFILE)).execute(context);
            @SuppressWarnings("unchecked")
            final O output = (O) context.get(ExecutorTags.OUTPUT);
            return output;
        } catch (final Exception exp) {
            throw CommonException.wrap(
                exp,
                () -> new ActionException(action, exp),
                ActionException.class
            );
        }
    }

    @Override
    public void addAction(final Action<?, ?> action) {
        this.lock.lock();
        for (final KeyComputer computer : this.computers) {
            this.actions.put(computer.compute(action), action);
        }
        this.lock.unlock();
    }
}
