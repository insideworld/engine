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

package insideworld.engine.security.common.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.exception.CommonException;
import insideworld.engine.security.common.AuthenticationException;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.storages.UserStorage;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class TokenAuth implements Auth<TokenContainer> {

    private final UserStorage users;

    @Inject
    public TokenAuth(final UserStorage users) {
        this.users = users;
    }

    @Override
    public void performAuth(final Record context, final TokenContainer token)
        throws CommonException {
        if (context.contains(UserTags.USER)) {
            throw new AuthenticationException("You are so smart, isn't?");
        }
        context.put(UserTags.USER, this.users.getByToken(token.getToken()));
    }
}
