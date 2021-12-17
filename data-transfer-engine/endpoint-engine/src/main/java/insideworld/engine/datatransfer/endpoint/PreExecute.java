package insideworld.engine.datatransfer.endpoint;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;

public interface PreExecute<T> {

    void preExecute(Record context, T parameter) throws Exception;

}
