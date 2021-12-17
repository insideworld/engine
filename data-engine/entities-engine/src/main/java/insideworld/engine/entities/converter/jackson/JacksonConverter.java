package insideworld.engine.entities.converter.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.MapRecord;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.EntityConverter;
import insideworld.engine.entities.storages.StorageException;
import insideworld.engine.entities.storages.keeper.StorageKeeper;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.injection.ObjectFactory;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Deprecated
//@Singleton
public class JacksonConverter implements EntityConverter {

    private final ObjectMapper mapper;
    private final StorageKeeper storages;
    private final ObjectFactory factory;

    @Inject
    public JacksonConverter(final StorageKeeper storages,
                            final ObjectFactory factory) {
        this.mapper = new ObjectMapper();
        this.mapper.addMixIn(Entity.class, EntityMixIn.class);
        this.mapper.setPropertyNamingStrategy(new ReplaceNamingStrategy());
        this.storages = storages;
        this.factory = factory;
    }

    @Override
    public Record convert(final Entity entity) throws ActionException {
        final Record record = this.factory.createObject(Context.class);
        final Map<String, Object> map;
        try {
        map = this.mapper.convertValue(this.getMap(entity), Map.class);
        } catch (final IntrospectionException | IllegalAccessException | InvocationTargetException exp) {
            throw new ActionException(
                String.format("Can't convert entity with type %s", entity.getClass()),
                exp
            );
        }
        map.forEach(record::put);
        return record;
    }

    @Override
    public <T extends Entity> T convert(final Record record, final Class<T> type) throws ActionException {
        final Long id = record.get(StorageTags.ID);
        try {
            if (id != null && !this.storages.getStorage(type).exists(id)) {
                throw new StorageException(
                    String.format("Entity with ID %s for type %s is not exists", id, type)
                );
            }
        } catch (final StorageException exp) {
            throw new ActionException("Can't read message", exp);
        }
        return this.mapper.convertValue(record.values(), type);
    }

    private Map<String, Object> getMap(final Entity entity) throws
        IllegalAccessException, IntrospectionException, InvocationTargetException {
        final PropertyDescriptor[] descriptors =
            Introspector.getBeanInfo(entity.getClass()).getPropertyDescriptors();
        final Map<String, Object> map = Maps.newHashMapWithExpectedSize(descriptors.length);
        for (final var descriptor : descriptors) {
            if (descriptor.getName().equals("class")) {
                continue;
            }
//            if (Entity.class.isAssignableFrom(descriptor.getPropertyType())) {
//                map.put(descriptor.getName() + "Id", descriptor.getReadMethod().invoke(entity));
//            } else {
                map.put(descriptor.getName(), descriptor.getReadMethod().invoke(entity));
//            }
        }
        return map;
    }

}
