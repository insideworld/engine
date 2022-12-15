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

package insideworld.engine.core.security.token.base;

import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.security.core.SecurityException;
import insideworld.engine.core.security.core.auth.Auth;
import insideworld.engine.core.security.core.data.User;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractTokenAuth implements Auth {

    private final TokenStorage storage;

    @Inject
    public AbstractTokenAuth(final TokenStorage storage) {
        this.storage = storage;
    }

    @Override
    public User auth(final Context context) throws SecurityException {
        final String authorization = this.getToken(context);
        final User user;
        if (StringUtils.isEmpty(authorization) || !authorization.startsWith("Bearer")) {
            user = null;
        } else {
            user = this.storage.getByToken(authorization.substring(7))
                .orElseThrow(() -> new SecurityException("Token not found"))
                .getUser();
        }
        return user;
    }

    protected abstract String getToken(Context parameter) throws SecurityException;
}