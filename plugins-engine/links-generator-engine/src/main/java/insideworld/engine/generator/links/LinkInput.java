package insideworld.engine.generator.links;

import insideworld.engine.actions.keeper.context.Context;

public interface LinkInput {

    void setContext(Context context);

    Context getContext();

}
