package insideworld.engine.security.common.auth;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import javax.naming.AuthenticationException;

public interface Auth<T> {

    void performAuth(Record context, T auth) throws AuthenticationException;

}
