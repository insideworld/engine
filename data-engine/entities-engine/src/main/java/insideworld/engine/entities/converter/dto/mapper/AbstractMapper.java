package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import java.lang.reflect.Method;

public abstract class AbstractMapper implements Mapper {

    protected final void write(final Entity entity,
                               final Object target,
                               final Descriptor descriptor) throws ActionException {
        try {
            final Method method = descriptor.getMethod();
            if (method != null) {
                method.invoke(entity, target);
            }
        } catch (final Exception exp) {
            throw new ActionException(
                String.format("Can't write field %s with type %s from %s",
                    descriptor.getName(), descriptor.getType(), entity.getClass()
                ),
                exp
            );
        }
    }

    protected final Object read(final Entity entity,
                                final Descriptor descriptor) throws ActionException {
        final Method method = descriptor.getMethod();
        final Object target;
        if (method != null) {
            try {
                target = method.invoke(entity);
            } catch (final Exception exp) {
                throw new ActionException(
                    String.format("Can't read field %s with type %s from %s",
                        descriptor.getName(), descriptor.getType(), entity.getClass()
                    ),
                    exp
                );
            }
        } else {
            target = null;
        }
        return target;
    }

}
