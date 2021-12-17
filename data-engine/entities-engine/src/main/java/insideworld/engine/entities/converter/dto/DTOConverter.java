package insideworld.engine.entities.converter.dto;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.MapRecord;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.EntityConverter;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Simple DTO converter.
 *
 * Deprecated.
 * Use
 * @see insideworld.engine.entities.converter.jackson.JacksonConverter
 */
@Singleton
public class DTOConverter implements EntityConverter {

    private final Descriptors descriptors;
    private final ObjectFactory factory;
    private final StorageKeeper storages;

    @Inject
    public DTOConverter(final Descriptors descriptors,
                        final ObjectFactory factory,
                        final StorageKeeper storages) {
        this.descriptors = descriptors;
        this.factory = factory;
        this.storages = storages;
    }

    @Override
    public Record convert(final Entity entity) throws ActionException {
        final Record record = this.factory.createObject(Context.class);
        for (final var descriptor : this.descriptors.getDescriptors(entity.getClass())) {
            descriptor.getLeft().toRecord(record, entity, descriptor.getRight());
        }
        return record;
    }

    @Override
    public <T extends Entity> T convert(final Record record, final Class<T> type)
        throws ActionException {
        final T entity;
        try {
            entity = this.readEntity(record, type);
        } catch (final StorageException exp) {
            throw new ActionException("Can't read entity", exp);
        }
        for (final var descriptor : this.descriptors.getDescriptors(type)) {
            descriptor.getLeft().toEntity(record, entity, descriptor.getRight());
        }
        return entity;
    }

    private <T extends Entity> T readEntity(final Record context, final Class<T> type)
        throws StorageException {
        final Long id = context.get(StorageTags.ID);
        final T entity;
        if (id == null) {
            entity = this.factory.createObject(type);
        } else {
            final Storage<T> storage = this.storages.getStorage(type);
            entity = storage.read(id);
        }
        return entity;
    }
}
