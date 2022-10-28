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

package insideworld.engine.actions.executor.profiles;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.PreExecutor;
import insideworld.engine.actions.keeper.context.Context;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * System execute profile.
 * Use this profile to execute an action inside the same system (usually from jobs).
 * Also execute PreExecutes from DefaultExecuteProfile.
 * @see DefaultExecuteProfile
 * @since 0.1.0
 */
@Singleton
public class SystemExecuteProfile implements ExecuteProfile {

    /**
     * Default execute profile.
     */
    private final DefaultExecuteProfile def;
    /**
     * PreExecutors for this instance.
     */
    private final Collection<PreExecutor> executors;

    /**
     * Default constructor.
     * @param def Default execute profile.
     * @param executors Collection of all executors in the system.
     */
    @Inject
    public SystemExecuteProfile(final DefaultExecuteProfile def,
                                final Collection<PreExecutor> executors) {
        this.def = def;
        this.executors = executors.stream()
            .filter(executor -> executor.forProfile().contains(SystemExecuteProfile.class))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public final void preExecute(final Context context) throws ActionException {
        for (final PreExecutor executor : this.executors) {
            executor.preExecute(context);
        }
        this.def.preExecute(context);
    }
}
