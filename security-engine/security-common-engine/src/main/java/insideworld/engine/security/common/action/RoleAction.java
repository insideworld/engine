package insideworld.engine.security.common.action;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.security.common.entities.Role;
import java.util.Collection;

public interface RoleAction extends Action {

    /**
     * Role for current action.
     * @param context
     * @return
     * @throws ActionException
     */
    Collection<Role> role(Context context) throws ActionException;

}
