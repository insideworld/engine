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
public class ReadDescriptors extends AbstractDescriptors {

    @Inject
    public ReadDescriptors(final Collection<Mapper> mappers) {
        super(mappers);
    }

    @Override
    protected Descriptor createDescriptor(final PropertyDescriptor descriptor) {
        final Descriptor result;
        final Method method = descriptor.getReadMethod();
        if (method == null || method.isAnnotationPresent(JsonIgnore.class)) {
            result = null;
        } else {
            result = new Descriptor(
                descriptor.getName(),
                method.getReturnType(),
                method.getGenericReturnType(),
                method
            );
        }
        return result;
    }
}
