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
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;

/**
 * Tag mapper for base entity.
 *
 * @since 0.0.1
 */
@Singleton
public class MapperEntities extends AbstractMapper {

    private final StorageKeeper storages;

    @Inject
    public MapperEntities(final StorageKeeper storages) {
        this.storages = storages;
    }

    @Override
    public void toEntity(Record record, Entity entity, Descriptor descriptor) throws ActionException {
        final String tag = this.defineTag(descriptor.getName());
        if (record.contains(tag)) {
            final Class<?> type = this.getGeneric(descriptor);
            final Collection<Long> ids = record.get(tag);
            final Collection<? extends Entity> target;
            try {
                target = this.storages
                    .getStorage((Class<? extends Entity>) type)
                    .read(ids);
            } catch (final StorageException exp) {
                throw new ActionException(String.format("Can't read %s by %s", type, ids), exp);
            }
            this.write(entity, target, descriptor);
        } else {
            this.write(entity, null, descriptor);
        }
    }

    @Override
    public void toRecord(final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        final Collection<Entity> entities = (Collection<Entity>) this.read(entity, descriptor);
        if (CollectionUtils.isNotEmpty(entities)) {
            record.put(
                this.defineTag(descriptor.getName()),
                entities.stream().map(Entity::getId).collect(Collectors.toUnmodifiableList())
            );
        }
    }

    @Override
    public boolean canApply(final Descriptor descriptor) {
        return Collection.class.isAssignableFrom(descriptor.getType()) &&
            Entity.class.isAssignableFrom(this.getGeneric(descriptor));
    }

    private Class<?> getGeneric(final Descriptor descriptor) {
        return (Class<?>) ((ParameterizedType) descriptor.getParameterized()).getActualTypeArguments()[0];
    }

    private String defineTag(final String origin) {
        return String.format("%sIds", origin);
    }
}
