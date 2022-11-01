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

package insideworld.engine.security.common.system;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.executor.profiles.ExecuteWrapper;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.executor.profiles.SystemExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.properties.PropertiesException;
import insideworld.engine.properties.PropertiesProvider;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;

///**
// * Using for preauth with system user to launch system action.
// */
//@Singleton
//public class SystemPreExecuteWrapper implements ExecuteWrapper {
//
//    private final User user;
//
//    @Inject
//    public SystemPreExecuteWrapper(final PropertiesProvider properties,
//                                   final UserStorage storage) throws PropertiesException {
//        final String username = properties.provide("engine.system.username", String.class);
//        this.user = storage.getByName(username)
//            .orElseThrow(() -> new PropertiesException("Can't find system user"));
//    }
//
//    @Override
//    public void execute(final Context context) throws ActionException {
//        context.put(UserTags.USER, this.user);
//    }
//
//    @Override
//    public Collection<Class<? extends ExecuteProfile>> forProfile() {
//        return Collections.singleton(SystemExecuteProfile.class);
//    }
//}
