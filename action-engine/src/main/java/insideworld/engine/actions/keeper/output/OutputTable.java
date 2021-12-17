package insideworld.engine.actions.keeper.output;

import insideworld.engine.actions.keeper.ListTable;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.injection.ObjectFactory;
import java.io.Serializable;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class OutputTable extends ListTable implements Output, Serializable {

    @Inject
    public OutputTable(final ObjectFactory factory) {
        super(factory);
    }

    @Override
    protected Class<? extends Record> recordType() {
        return OutputRecord.class;
    }


}

