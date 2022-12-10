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

package insideworld.engine.security.token.base;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.executor.profiles.AbstractExecuteWrapper;
import insideworld.engine.actions.executor.profiles.ExecuteProfile;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.exception.CommonException;
import insideworld.engine.security.core.SecurityException;
import insideworld.engine.security.core.UserTags;
import insideworld.engine.security.core.entities.User;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractTokenWrapper<T> extends AbstractExecuteWrapper {

    private final TokenUserStorage users;

    public AbstractTokenWrapper(final TokenUserStorage users) {
        this.users = users;
    }

    @Override
    public void execute(final Action action, final Context context, final Output output)
        throws CommonException {
        if (context.contains(UserTags.USER)) {
            throw new SecurityException("You are so smart, isn't?");
        }
        context.put(
            UserTags.USER,
            this.users
                .getByToken(this.getToken(context))
                .orElseThrow(() -> new SecurityException("Token not found"))
            );
        super.execute(action, context, output);
    }

    @Override
    public long wrapperOrder() {
        return 1_200_000;
    }

    protected abstract String getToken(final Context context);
}
