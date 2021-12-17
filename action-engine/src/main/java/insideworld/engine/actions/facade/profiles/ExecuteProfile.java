package insideworld.engine.actions.facade.profiles;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.PreExecutor;
import insideworld.engine.actions.keeper.context.Context;
import java.util.Collection;

public interface ExecuteProfile {

    void preExecute(Context context) throws ActionException;

}
