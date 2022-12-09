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

package insideworld.engine.security.common.stub;

import insideworld.engine.data.generator.inmemory.storage.abstracts.AbstractMemoryStorage;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.exception.CommonException;
import insideworld.engine.injection.ObjectFactory;
import insideworld.engine.security.common.entities.Role;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Create test user on start application.
 *
 * @since 1.0.0
 */
@Singleton
public class CreateTestUser implements OnStartUp {

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * User storage.
     */
    private final UserStorage users;

    /**
     * Role storage.
     */
    private final Storage<Role> roles;

    /**
     * Constructor.
     *
     * @param factory Object factory.
     * @param users User storage.
     * @param roles Role storage.
     */
    @Inject
    public CreateTestUser(
        final ObjectFactory factory,
        final UserStorage users,
        final Storage<Role> roles
    ) {
        this.factory = factory;
        this.users = users;
        this.roles = roles;
    }

    @Override
    public final void startUp() throws CommonException {
        final Role role = this.roles.read(1);
        role.setName("testrole");
        this.roles.write(role);
        final User user = this.users.read(1);
        user.setName("testuser");
        user.setToken("testtoken");
        user.setRole(role);
        this.users.write(user);
        final User system = this.users.read(2);
        system.setName("testsystem");
        system.setToken("testsystemtoken");
        system.setRole(role);
    }

    @Override
    public long startOrder() {
        return 5_000_000;
    }
}
