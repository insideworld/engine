/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.converter.dto;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.EntityConverter;
import insideworld.engine.entities.converter.dto.descriptors.AbstractDescriptors;
import insideworld.engine.entities.converter.dto.descriptors.ReadDescriptors;
import insideworld.engine.entities.converter.dto.descriptors.WriteDescriptors;
import insideworld.engine.entities.storages.Storage;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Simple DTO converter.
 *
 * Deprecated.
 * Use
 * @see insideworld.engine.entities.converter.jackson.JacksonConverter
 */
@Singleton
public class DTOConverter implements EntityConverter {

    private final ReadDescriptors reads;
    private final WriteDescriptors writes;
    private final ObjectFactory factory;
    private final StorageKeeper storages;

    @Inject
    public DTOConverter(final ReadDescriptors reads,
                        final WriteDescriptors writes,
                        final ObjectFactory factory,
                        final StorageKeeper storages) {
        this.reads = reads;
        this.writes = writes;
        this.factory = factory;
        this.storages = storages;
    }

    @Override
    public Record convert(final Entity entity) throws ActionException {
        final Record record = this.factory.createObject(Context.class);
        for (final var descriptor : this.reads.getDescriptors(entity.getClass())) {
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
        for (final var descriptor : this.writes.getDescriptors(type)) {
            descriptor.getLeft().toEntity(record, entity, descriptor.getRight());
        }
        return entity;
    }

    private <T extends Entity> T readEntity(final Record context, final Class<T> type)
        throws StorageException {
        final Long id = context.get(StorageTags.ID);
        final T entity;
        if (id == null || id.equals(0L)) {
            entity = this.factory.createObject(type);
        } else {
            final Storage<T> storage = this.storages.getStorage(type);
            entity = storage.read(id);
        }
        return entity;
    }
}
