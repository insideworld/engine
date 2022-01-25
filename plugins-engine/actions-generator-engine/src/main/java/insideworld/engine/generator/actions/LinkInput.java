package insideworld.engine.generator.actions;

import insideworld.engine.actions.keeper.context.Context;

public interface LinkInput {

    void setContext(Context context);

    Context getContext();

}
