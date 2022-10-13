/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.converter.dto.descriptors;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;


public abstract class AbstractDescriptors {

    /**
     *
     */
    public final Map<Class<? extends Entity>, Pair<Mapper, Descriptor>[]> descriptors =
        Maps.newConcurrentMap();

    private final Collection<Mapper> mappers;

    @Inject
    public AbstractDescriptors(final Collection<Mapper> mappers) {
        this.mappers = mappers;
    }

    public Pair<Mapper, Descriptor>[] getDescriptors(final Class<? extends Entity> type) {
        return this.descriptors.containsKey(type) ?
            this.descriptors.get(type) :
            this.getDescriptorsSync(type);
    }

    //TODO: Make rental lock.
    private synchronized Pair<Mapper, Descriptor>[] getDescriptorsSync(
        final Class<? extends Entity> type) {
        if (!this.descriptors.containsKey(type)) {
            this.descriptors.put(type, this.createDescriptors(type));
        }
        return this.descriptors.get(type);
    }

    private Pair<Mapper, Descriptor>[] createDescriptors(final Class<? extends Entity> type) {
        try {
            final var beans = Introspector.getBeanInfo(type).getPropertyDescriptors();
            final Collection<Pair<Mapper, Descriptor>> result =
                Lists.newArrayListWithCapacity(beans.length);
            for (final var bean : beans) {
                final Descriptor descriptor = this.createDescriptor(bean);
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
            return result.toArray(new Pair[result.size()]);
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
    }

    protected abstract Descriptor createDescriptor(PropertyDescriptor descriptor);
}


