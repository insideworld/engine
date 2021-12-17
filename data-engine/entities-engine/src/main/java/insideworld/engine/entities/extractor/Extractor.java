package insideworld.engine.entities.extractor;

import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import java.util.Map;

public interface Extractor {

    Output extract(Map<String, ?> context, String schema);

    Output extract(Context context, String schema);

    int execute(Map<String, ?> context, String schema);

    int execute(Context context, String schema);
}
