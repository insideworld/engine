package insideworld.engine.entities.actions.links;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionRuntimeException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collections;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class DeleteEntityLink<T extends Entity> implements Link {

    private final StorageKeeper storages;
    private Storage<T> storage;
    private EntityTag<T> single;
    private EntitiesTag<T> multiple;

    @Inject
    public DeleteEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.single != null && context.contains(this.single)) {
            this.storage.delete(Collections.singleton(context.get(this.single)));
        } else {
            if (this.multiple != null && context.contains(this.multiple)) {
                this.storage.delete(context.get(this.multiple));
            }
        }
    }

    /**
     * Set single tag to read.
     * @param read ID tag with entity ID.
     * @param put Entity tag to put in context.
     * @return The same instance.
     */
    public DeleteEntityLink<T> setTag(final EntityTag<T> single) {
        this.single = single;
        return this;
    }

    /**
     * Set multiple tag to read.
     * @param read ID's tag with entity ID's.
     * @param put Entities tag to put in context.
     * @return The same instance.
     */
    public DeleteEntityLink<T> setTags(final EntitiesTag<T> multiple) {
        this.multiple = multiple;
        return this;
    }

    /**
     * Set type of entity.
     * @param type Entity type.
     * @return The same instance
     */
    public DeleteEntityLink<T> setType(final Class<T> type) {
        try {
            this.storage = this.storages.getStorage(type);
        } catch (final StorageException exp) {
            throw new ActionRuntimeException(new ActionException("Can't find storage", exp));
        }
        return this;
    }
}
