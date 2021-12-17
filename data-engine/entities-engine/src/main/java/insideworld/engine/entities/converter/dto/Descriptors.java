package insideworld.engine.entities.converter.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

@Singleton
public class Descriptors {

    /**
     * Map for types.
     */
    public final Map<Class<? extends Entity>, Pair<Mapper, Descriptor>[]> descriptors =
        Maps.newConcurrentMap();
    private final Collection<Mapper> mappers;

    @Inject
    public Descriptors(final Collection<Mapper> mappers) {
        this.mappers = mappers;
    }

    public Pair<Mapper, Descriptor>[] getDescriptors(final Class<? extends Entity> type) {
        final Pair<Mapper, Descriptor>[] array;
        if (this.descriptors.containsKey(type)) {
            array = this.descriptors.get(type);
        } else {
            array = this.getDescriptorsSync(type);
        }
        return array;
    }


    private synchronized Pair<Mapper, Descriptor>[] getDescriptorsSync(final Class<? extends Entity> type) {
        final Pair<Mapper, Descriptor>[] array;
        if (this.descriptors.containsKey(type)) {
            array = this.descriptors.get(type);
        } else {
            array = this.createDescriptors(type);
        }
        return array;
    }

    private Pair<Mapper, Descriptor>[] createDescriptors(final Class<? extends Entity> type) {
        try {
            final var beans = Introspector.getBeanInfo(type).getPropertyDescriptors();
            final Collection<Pair<Mapper, Descriptor>> result =
                Lists.newArrayListWithCapacity(beans.length);
            for (final var bean : beans) {
                final Field field = this.findField(bean.getName(), type);;
                if (field == null || field.isAnnotationPresent(JsonIgnore.class)) {
                    continue;
                }
                final Descriptor descriptor = new Descriptor(
                    bean.getReadMethod(), bean.getWriteMethod(), field);
                for (final var mapper : this.mappers) {
                    if (mapper.canApply(descriptor)) {
                        result.add(Pair.of(mapper, descriptor));
                    }
                }
            }
            return result.toArray(new Pair[result.size()]);
        } catch (final IntrospectionException exp) {
            throw new RuntimeException(exp);
        }
    }

    private Field findField(final String name, final Class<?> type) {
        Field field;
        try {
            field = type.getDeclaredField(name);
        } catch (final NoSuchFieldException exp) {
            if (type.getSuperclass() == null) {
                field = null;
            } else {
                field = this.findField(name, type.getSuperclass());
            }
        }
        return field;
    }
}


