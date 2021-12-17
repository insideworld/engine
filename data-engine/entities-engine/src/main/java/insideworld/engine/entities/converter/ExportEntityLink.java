package insideworld.engine.entities.converter;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Export entity to DTO representation.
 */
@Dependent
public class ExportEntityLink implements Link {

    private final EntityConverter converter;

    private EntityTag<?> single;

    private EntitiesTag<?> multiple;

    @Inject
    public ExportEntityLink(final EntityConverter converter) {
        this.converter = converter;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        final Collection<Entity> entities = Lists.newLinkedList();
        context.optional(this.single).ifPresent(entities::add);
        context.optional(this.multiple).ifPresent(entities::addAll);
        for (final Entity entity : entities) {
            output.add(this.converter.convert(entity));
        }
    }

    public ExportEntityLink setTag(final EntityTag<?> tag) {
        this.single = tag;
        return this;
    }

    public ExportEntityLink setTag(final EntitiesTag<?> tag) {
        this.multiple = tag;
        return this;
    }
}
