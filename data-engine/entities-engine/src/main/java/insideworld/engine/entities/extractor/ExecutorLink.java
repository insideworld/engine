package insideworld.engine.entities.extractor;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.tags.StorageTags;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;

@Dependent
public class ExecutorLink implements Link {

    private final Extractor extractor;
    private String schema;

    @Inject
    public ExecutorLink(final Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        Validate.notNull(this.schema, "You didn't init a schema!");
        output.createRecord(this.schema)
            .put(StorageTags.COUNT, this.extractor.execute(context, this.schema));
    }

    public void setSchema(final String schema) {
        this.schema = schema;
    }
}
