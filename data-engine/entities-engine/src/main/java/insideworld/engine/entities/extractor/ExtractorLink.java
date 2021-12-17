package insideworld.engine.entities.extractor;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.Validate;

@Dependent
public class ExtractorLink implements Link {

    private final Extractor extractor;
    private String schema;

    @Inject
    public ExtractorLink(final Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        Validate.notNull(this.schema, "You didn't init a schema!");
        final Output result = this.extractor.extract(context, this.schema);
        output.merge(result);
    }

    public void setSchema(final String schema) {
        this.schema = schema;
    }
}
