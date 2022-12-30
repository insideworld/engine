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

package insideworld.engine.core.data.core.converter.dto.descriptors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.core.data.core.Entity;
import insideworld.engine.core.data.core.StorageException;
import insideworld.engine.core.data.core.converter.dto.mapper.Mapper;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Abstract descriptors.
 * Using to describe fields in entity to provide work with it.
 * @since 0.0.1
 */
public abstract class AbstractDescriptors {

    /**
     * Object for lock.
     */
    private final Object lock;

    /**
     * Map of descriptors.
     * Key - type of entity.
     * Value - array of pair mapper and descriptor.
     */
    private final Map<Class<? extends Entity>, Pair<Mapper, Descriptor>[]> descriptors;

    /**
     * Collection of all mappers.
     */
    private final Collection<Mapper> mappers;

    /**
     * Default constructor.
     * @param mappers Mappers.
     */
    @Inject
    public AbstractDescriptors(final Collection<Mapper> mappers) {
        this.mappers = mappers;
        this.descriptors = Maps.newConcurrentMap();
        this.lock = new Object();
    }

    /**
     * Get a pair of descriptors by entity.
     * @param type Entity type.
     * @return Pairs of mapper and descriptor.
     * @throws StorageException Can't init descriptor.
     */
    public final Pair<Mapper, Descriptor>[] getDescriptors(final Class<? extends Entity> type)
        throws StorageException {
        final Pair<Mapper, Descriptor>[] result;
        if (this.descriptors.containsKey(type)) {
            result = this.descriptors.get(type);
        } else {
            result = this.getDescriptorsSync(type);
        }
        return result;
    }

    /**
     * Create custom descriptor from property descriptor.
     * @param parent Parent entity.
     * @param descriptor Java beans descriptor.
     * @return Custom descriptor.
     */
    protected abstract Descriptor createDescriptor(
        Class<? extends Entity> parent, PropertyDescriptor descriptor);

    /**
     * Sync method to cache descriptors internally.
     * @param type Entity type.
     * @return Pairs of mapper and descriptor.
     * @throws StorageException Can't init descriptor.
     */
    private Pair<Mapper, Descriptor>[] getDescriptorsSync(final Class<? extends Entity> type)
        throws StorageException {
        synchronized (this.lock) {
            if (!this.descriptors.containsKey(type)) {
                this.descriptors.put(type, this.createDescriptors(type));
            }
            return this.descriptors.get(type);
        }
    }

    /**
     * Create a pair of mapper and descriptor for specific entity.
     * Using Introspector to take all information about field, getters and setters.
     * @param type Entity type.
     * @return Pairs of mapper and descriptor.
     * @throws StorageException Can't init descriptor.
     */
    private Pair<Mapper, Descriptor>[] createDescriptors(final Class<? extends Entity> type)
        throws StorageException {
        try {
            final var beans = Introspector.getBeanInfo(type)
                .getPropertyDescriptors();
            final Collection<Pair<Mapper, Descriptor>> result =
                Lists.newArrayListWithCapacity(beans.length);
            for (final var bean : beans) {
                final Descriptor descriptor = this.createDescriptor(type, bean);
                if (descriptor == null) {
                    continue;
                }
                for (final var mapper : this.mappers) {
                    if (mapper.canApply(descriptor)) {
                        result.add(Pair.of(mapper, descriptor));
                        break;
                    }
                }
            }
            return result.toArray(new Pair[0]);
        } catch (final IntrospectionException exp) {
            throw new StorageException(exp, "Can't read beans for type %s", type.getName());
        }
    }
}
