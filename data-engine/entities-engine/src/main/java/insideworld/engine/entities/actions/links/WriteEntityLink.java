package insideworld.engine.entities.actions.links;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class WriteEntityLink<T extends Entity> implements Link {

    private final StorageKeeper storages;
    private EntityTag<T> single;
    private EntitiesTag<T> multiple;
    private boolean count = false;

    @Inject
    public WriteEntityLink(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.single != null && context.contains(this.single)) {
            try {
                context.put(this.single, this.writeSingle(context.get(this.single)), true);
            } catch (final StorageException exp) {
                throw new ActionException(context, exp);
            }
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.single.getTag());
                record.put("count", 1);
            }
        }
        if (this.multiple != null && context.contains(this.multiple)) {
            try {
                context.put(this.multiple, this.writeMultiple(context.get(this.multiple)), true);
            } catch (final StorageException exp) {
                throw new ActionException(context, exp);
            }
            if (this.count) {
                final Record record = output.createRecord();
                record.put("type", this.multiple.getTag());
                record.put("count", context.get(this.multiple).size());
            }
        }
    }

    private T writeSingle(final T entity) throws StorageException {
        final Storage<T> storage = (Storage<T>) this.storages.getStorage(entity.getClass());
        return storage.write(entity);
    }

    private Collection<T> writeMultiple(final Collection<T> collection)
        throws StorageException {
        final Collection<T> results;
        final Optional<T> entity = collection.stream().findAny();
        if (entity.isPresent()) {
            final Storage<T> storage = (Storage<T>) this.storages.getStorage(entity.get().getClass());
            results = storage.writeAll(collection);
        } else {
            results = Collections.emptyList();
        }
        return results;
    }

    public WriteEntityLink<T> setTag(final EntityTag<T> tag) {
        this.single = tag;
        return this;
    }

    public WriteEntityLink<T> setTag(final EntitiesTag<T> tag) {
        this.multiple = tag;
        return this;
    }

    public WriteEntityLink<T> count() {
        this.count = true;
        return this;
    }

}
