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

package insideworld.engine.security.core.action;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.profiles.AbstractExecuteWrapper;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.exception.CommonException;
import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import insideworld.engine.security.core.UserTags;
import insideworld.engine.security.core.entities.User;
import insideworld.engine.security.core.storages.UserStorage;
import insideworld.engine.startup.OnStartUp;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Using to set system user in context for internal calls.
 * @since 1.0.0
 */
@Singleton
public class SystemUserExecuteWrapper extends AbstractExecuteWrapper implements OnStartUp {

    /**
     * System user.
     */
    private User user;
    private final PropertiesProvider properties;
    private final UserStorage<User> storage;

    /**
     * Constructor.
     * @param properties Properties provider.
     * @param storage User storage.
     * @throws PropertiesException Property of system user is not set.
     */
    @Inject
    public SystemUserExecuteWrapper(final PropertiesProvider properties,
                                    final UserStorage<User> storage)  {
        this.properties = properties;
        this.storage = storage;
    }

    @Override
    public final void execute(final Action action, final Context context, final Output output)
        throws CommonException {
        context.put(UserTags.USER, this.user);
        super.execute(action, context, output);
    }

    @Override
    public void startUp() throws CommonException {
        this.user = this.storage
            .getByName(properties.provide("engine.system.username", String.class))
            .orElseThrow(() -> new PropertiesException("Can't find system user"));
    }

    @Override
    public final long startOrder() {
        return 1_500_000;
    }

    @Override
    public long wrapperOrder() {
        return 1_500_000;
    }

    @Override
    public final Collection<Class<? extends ExecuteProfile>> forProfile() {
        return Collections.singleton(SystemExecuteProfile.class);
    }
}
