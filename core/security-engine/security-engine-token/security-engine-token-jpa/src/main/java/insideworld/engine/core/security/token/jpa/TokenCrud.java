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

package insideworld.engine.core.security.token.jpa;

import insideworld.engine.core.security.core.data.User;
import insideworld.engine.core.security.token.base.Token;
import insideworld.engine.core.security.token.base.TokenStorage;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import java.util.Optional;
import javax.inject.Singleton;

@Singleton
public class TokenCrud implements PanacheRepository<TokenJpa>, TokenStorage {

    @Override
    public Optional<Token> getByToken(final String token) {
        return Optional.ofNullable(
            find("token", token)
                .withHint("org.hibernate.cacheable", Boolean.TRUE)
                .firstResult()
        );
    }

    @Override
    public Token getByUser(final User user) {
        return find("user", user)
            .withHint("org.hibernate.cacheable", Boolean.TRUE)
            .firstResult();
    }

    @Override
    public Token write(Token token) {
        final TokenJpa crud = TokenJpa.class.cast(token);
        final TokenJpa merged;
        if (isPersistent(crud)) {
            merged = crud;
        } else {
            merged = getEntityManager().merge(crud);
        }
        persist(merged);
        return merged;
    }
}
