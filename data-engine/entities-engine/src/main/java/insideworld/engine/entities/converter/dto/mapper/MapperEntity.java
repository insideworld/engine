package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.Descriptor;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.Validate;

/**
 * Tag mapper for base entity.
 *
 * @since 0.0.1
 */
@Singleton
public class MapperEntity extends AbstractMapper {

    private final StorageKeeper storages;

    @Inject
    public MapperEntity(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public void toEntity(final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final String tag = this.defineTag(descriptor.getField().getName());
        if (record.contains(tag)) {
            final Class<?> type = descriptor.getField().getType();
            Validate.isTrue(Entity.class.isAssignableFrom(type));
            final long id = record.get(tag);
            final Entity target;
            try {
                target = this.storages
                    .getStorage((Class<? extends Entity>) type)
                    .read(id);
            } catch (final StorageException exp) {
                throw new ActionException(String.format("Can't read %s by %s", type, id), exp);
            }
            this.write(entity, target, descriptor);
        } else {
            this.write(entity, null, descriptor);
        }
    }

    @Override
    public void toRecord(final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final Entity target = (Entity) this.read(entity, descriptor);
        if (target != null) {
            record.put(this.defineTag(descriptor.getField().getName()), target.getId());
        }
    }

    @Override
    public boolean canApply(final Descriptor descriptor) {
        return Entity.class.isAssignableFrom(descriptor.getField().getType());
    }

    private String defineTag(final String origin) {
        return String.format("%sId", origin);
    }
}
