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

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.StorageException;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Mapper for entity.
 * Will add or take in record value with key field name + postfix Id.
 *
 * @since 0.0.1
 */
@Singleton
public class MapperEntity extends AbstractMapper<Long, Entity> {

    /**
     * Storage keeper.
     */
    private final StorageKeeper storages;

    /**
     * Default constructor.
     *
     * @param storages Storage keeper.
     */
    @Inject
    public MapperEntity(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        return Entity.class.isAssignableFrom(descriptor.type());
    }

    @Override
    protected final Entity toEntity(final Long target, final Descriptor descriptor)
        throws StorageException {
        final Entity entity;
        if (target == null) {
            entity = null;
        } else {
            entity = this.storages
                .getStorage((Class<? extends Entity>) descriptor.type())
                .read(target);
        }
        return entity;
    }

    @Override
    protected final Long toRecord(final Entity value, final Descriptor descriptor) {
        return value.getId();
    }

    @Override
    protected final String defineTag(final String origin) {
        return String.format("%sId", origin);
    }
}
