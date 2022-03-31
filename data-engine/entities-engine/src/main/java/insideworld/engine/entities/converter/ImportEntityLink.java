package insideworld.engine.entities.converter;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ImportEntityLink implements Link {

    private final EntityConverter converter;
    private EntityTag<? extends Entity> tag;
    private Class<? extends Entity> type;

    @Inject
    public ImportEntityLink(final EntityConverter converter) {
        this.converter = converter;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (!context.contains(this.tag)) {
            final Entity entity = this.converter.convert(context, this.type);
            context.put(this.tag.getTag(), entity);
        }
    }

    public <T extends Entity> void setTag(final EntityTag<T> tag, final Class<T> type) {
        this.tag = tag;
        this.type = type;
    }
}
