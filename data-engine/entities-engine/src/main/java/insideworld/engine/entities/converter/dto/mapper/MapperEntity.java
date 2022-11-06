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

package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.Validate;

/**
 * Mapper for entity.
 * Will add or take in record value with key field name + postfix Id.
 * @since 0.0.1
 */
@Singleton
public class MapperEntity extends AbstractMapper {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Default constructor.
     * @param storages Storage keeper.
     */
    @Inject
    public MapperEntity(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final void toEntity(
        final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final String tag = this.defineTag(descriptor.name());
        if (record.contains(tag)) {
            final Class<?> type = descriptor.type();
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
    public final void toRecord(
        final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final Entity target = (Entity) this.read(entity, descriptor);
        if (target != null) {
            record.put(this.defineTag(descriptor.name()), target.getId());
        }
    }

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        return Entity.class.isAssignableFrom(descriptor.type());
    }

    /**
     * Define string key for record.
     * @param origin Field name.
     * @return Field name + Id
     */
    private static String defineTag(final String origin) {
        return String.format("%sId", origin);
    }
}
