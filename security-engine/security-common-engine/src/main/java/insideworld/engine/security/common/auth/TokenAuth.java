package insideworld.engine.security.common.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.storages.UserStorage;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

@Singleton
public class TokenAuth implements Auth<TokenContainer> {

    private final UserStorage users;

    @Inject
    public TokenAuth(final UserStorage users) {
        this.users = users;
    }

    @Transactional
    @Override
    public void performAuth(final Record context, final TokenContainer token)
        throws AuthenticationException {
        if (context.contains(UserTags.USER)) {
            throw new AuthenticationException("You are so smart, isn't?");
        }
        context.put(UserTags.USER, this.users.getByToken(token.getToken()));
    }
}
