package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import javax.enterprise.context.Dependent;

@Dependent
public class MarkOutputActions implements PostExecute {

    @Override
    public boolean apply(final Context context, final Output output) {
        output.forEach(record -> record.put(
            ActionsTags.FROM_ACTION,
            context.get(ActionsTags.ACTION).key())
        );
        return true;
    }
}
