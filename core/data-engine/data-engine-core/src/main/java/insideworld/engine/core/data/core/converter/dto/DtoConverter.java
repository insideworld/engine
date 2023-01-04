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

package insideworld.engine.core.data.core.converter.dto;

import insideworld.engine.core.common.keeper.Record;
import insideworld.engine.core.common.keeper.context.Context;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.converter.EntityConverter;
import insideworld.engine.core.data.core.converter.dto.descriptors.ReadDescriptors;
import insideworld.engine.core.data.core.converter.dto.descriptors.WriteDescriptors;
import insideworld.engine.core.data.core.storages.Storage;
import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
import insideworld.engine.core.common.injection.ObjectFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * DTO Entity converter
 * DTO entity - it's entity where all nested entity and entities
 * replaced to ID and ID's accordingly.
 * Because almost all entities has link with static record, no need to send it each time.
 * See package-info to more information about naming rules and instruction to use.
 *
 * @since 0.6.0
 */
@Singleton
public class DtoConverter implements EntityConverter {

    /**
     * Read descriptors.
     */
    private final ReadDescriptors reads;

    /**
     * Write descriptor.
     */
    private final WriteDescriptors writes;

    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Default constructor.
     *
     * @param reads Read descriptors.
     * @param writes Write descriptor.
     * @param factory Object factory.
     * @param storages Storage keeper.
     * @checkstyle ParameterNumberCheck (4 lines)
     */
    @Inject
    public DtoConverter(
        final ReadDescriptors reads, final WriteDescriptors writes,
        final ObjectFactory factory, final StorageKeeper storages
    ) {
        this.reads = reads;
        this.writes = writes;
        this.factory = factory;
        this.storages = storages;
    }

    @Override
    public final Record convert(final Entity entity) throws StorageException {
        final Record record = this.factory.createObject(Context.class);
        for (final var descriptor : this.reads.getDescriptors(entity.getClass())) {
            descriptor.getLeft().toRecord(record, entity, descriptor.getRight());
        }
        return record;
    }

    @Override
    public final <T extends Entity> T convert(final Record record, final Class<T> type)
        throws StorageException {
        final T entity = this.readEntity(record, type);
        for (final var descriptor : this.writes.getDescriptors(entity.getClass())) {
            descriptor.getLeft().toEntity(record, entity, descriptor.getRight());
        }
        return entity;
    }

    /**
     * Read entity using storage.
     * If ID null or 0 - will create a new object.
     *
     * @param context Record with DTO entity.
     * @param type Type of entity.
     * @param <T> Type of entity.
     * @return Entity.
     * @throws StorageException Can't find storage by id.
     */
    private <T extends Entity> T readEntity(final Record context, final Class<T> type)
        throws StorageException {
//        final Long id = context.get(StorageTags.ID);
//        final T entity;
//        if (id == null || id.equals(0L)) {
//            entity = this.factory.createObject(type);
//        } else {
//            final Storage<T> storage = this.storages.getStorage(type);
//            entity = storage.read(id);
//        }
//        if (entity == null) {
//            throw new StorageException(
//                "Can't read because storage return null",
//                type,
//                id
//            );
//        }
//        return entity;
        return null;
    }
}
