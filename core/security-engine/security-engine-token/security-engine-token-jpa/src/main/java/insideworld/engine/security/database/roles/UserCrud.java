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
import insideworld.engine.security.token.base.TokenUser;
import insideworld.engine.security.token.base.TokenUserStorage;
import java.util.Collection;
import java.util.Optional;
import javax.inject.Singleton;

@Singleton
public class UserCrud
    extends AbstractCrudGenericStorage<TokenUser, UserJpa> implements TokenUserStorage {

    @Override
    public Optional<TokenUser> getByToken(final String token) {
        return Optional.ofNullable(
            find("token", token)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResult()
        );
    }

    @Override
    public Optional<TokenUser> getByName(final String name) {
        return Optional.ofNullable(
            find("name", name)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResult()
        );
    }

    @Override
    public Collection<TokenUser> getByNames(final Collection<String> names) {
        return this.castUpper(
            find("name in (?1)", names)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .list()
        );
    }
}
