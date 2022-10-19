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

package insideworld.engine.security.database.roles;

import insideworld.engine.data.jpa.AbstractCrudGenericStorage;
import insideworld.engine.security.common.entities.User;
import insideworld.engine.security.common.storages.UserStorage;
import java.util.Collection;
import java.util.Optional;
import javax.inject.Singleton;
import javax.naming.AuthenticationException;

@Singleton
public class UserCrud extends AbstractCrudGenericStorage<User, UserJpa> implements UserStorage {

    @Override
    public User getByToken(final String token) throws AuthenticationException {
        return find("token", token)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResultOptional()
            .orElseThrow(() -> new AuthenticationException("Token not found"));
    }

    @Override
    public Optional<User> getByName(final String name) {
        return Optional.ofNullable(find("name", name)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResult());
    }

    @Override
    public Collection<User> getByNames(final Collection<String> names) {
        return this.castUpper(find("name in (?1)", names)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .list());
    }
}
