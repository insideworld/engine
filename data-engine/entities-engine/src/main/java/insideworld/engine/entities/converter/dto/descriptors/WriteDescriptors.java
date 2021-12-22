package insideworld.engine.entities.converter.dto.descriptors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WriteDescriptors extends AbstractDescriptors {

    @Inject
    public WriteDescriptors(final Collection<Mapper> mappers) {
        super(mappers);
    }

    @Override
    protected Descriptor createDescriptor(final PropertyDescriptor descriptor) {
        final Descriptor result;
        final Method method = descriptor.getWriteMethod();
        if (method == null || method.isAnnotationPresent(JsonIgnore.class)) {
            result = null;
        } else {
            result = new Descriptor(
                descriptor.getName(),
                method.getParameterTypes()[0],
                method.getGenericParameterTypes()[0],
                method
            );
        }
        return result;
    }
}
