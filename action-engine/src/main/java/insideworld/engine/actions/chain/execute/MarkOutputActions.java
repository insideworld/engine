package insideworld.engine.actions.chain.execute;

import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import javax.enterprise.context.Dependent;
import org.apache.commons.lang3.tuple.Pair;

@Dependent
public class MarkOutputActions implements PostExecute {

    @Override
    public boolean apply(Pair<Context, Context> contexts, Pair<Output, Output> outputs) {
        outputs.getRight().forEach(record -> record.put(
            ActionsTags.FROM_ACTION,
            contexts.getRight().get(ActionsTags.ACTION).key())
        );
        return true;
    }
}
