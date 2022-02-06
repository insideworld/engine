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


