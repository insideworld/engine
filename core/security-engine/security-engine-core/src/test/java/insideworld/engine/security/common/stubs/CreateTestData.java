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

package insideworld.engine.security.common.stubs;

import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.exception.CommonException;
import insideworld.engine.security.core.entities.Role;
import insideworld.engine.security.core.entities.User;
import insideworld.engine.security.core.storages.UserStorage;
import insideworld.engine.startup.OnStartUp;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Create test user on start application.
 *
 * @since 1.0.0
 */
@Singleton
public class CreateTestData implements OnStartUp {

    private int userids = 0;

    private int rolesids = 0;

    /**
     * User storage.
     */
    private final UserStorage<User> users;

    /**
     * Role storage.
     */
    private final Storage<Role> roles;

    /**
     * Constructor.
     *
     * @param users User storage.
     * @param roles Role storage.
     */
    @Inject
    public CreateTestData(
        final UserStorage<User> users,
        final Storage<Role> roles
    ) {
        this.users = users;
        this.roles = roles;
    }

    @Override
    public final void startUp() throws CommonException {
        final Role system = this.createRole(Roles.SYSTEM.getName(), null);
        this.createUser("testsystem", system);
        this.createUser(
            "one", this.createRole(Roles.ONE.getName(), system)
        );
        this.createUser(
            "two", this.createRole(Roles.TWO.getName(), system)
        );
        this.createUser(
            "three", this.createRole(Roles.THREE.getName(), system)
        );
    }

    private User createUser(final String name, final Role role)
        throws StorageException {
        final User user = this.users.read(this.userids++);
        user.setName(name);
        user.setRole(role);
        return this.users.write(user);
    }

    private Role createRole(final String name, final Role append) throws StorageException {
        final Role role = this.roles.read(this.rolesids++);
        role.setName(name);
        role.setAppend(append);
        return this.roles.write(role);
    }

    @Override
    public long startOrder() {
        return 5_000_000;
    }
}
