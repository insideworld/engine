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

package insideworld.engine.core.data.core.converter.dto.mapper;

import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.converter.dto.descriptors.Descriptor;
import insideworld.engine.core.data.core.storages.keeper.StorageKeeper;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Mapper for collection of entities.
 * Will add or take in record value with key field name + postfix Ids.
 *
 * @since 0.0.1
 */
@Singleton
public class MapperEntities extends AbstractMapper<Collection<Long>, Collection<Entity>> {

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
    public MapperEntities(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public final boolean canApply(final Descriptor descriptor) {
        return Collection.class.isAssignableFrom(descriptor.type())
            && Entity.class.isAssignableFrom(this.getGeneric(descriptor));
    }

    @Override
    protected final Collection<Entity> toEntity(
        final Collection<Long> target, final Descriptor descriptor) throws StorageException {
        final Class<? extends Entity> type = (Class<? extends Entity>) this.getGeneric(descriptor);
        final Collection<Entity> results;
        if (CollectionUtils.isEmpty(target)) {
            results = Collections.emptyList();
        } else {
            results = (Collection<Entity>) this.storages
                .getStorage((Class<? extends Entity>) type).read(target);
        }
        return results;
    }

    @Override
    protected final Collection<Long> toRecord(
        final Collection<Entity> value, final Descriptor descriptor) {
        final Collection<Long> results;
        if (CollectionUtils.isEmpty(value)) {
            results = null;
        } else {
            results = value.stream().map(Entity::getId).toList();
        }
        return results;
    }

    @Override
    protected final String defineTag(final String origin) {
        return String.format("%sIds", origin);
    }
}
