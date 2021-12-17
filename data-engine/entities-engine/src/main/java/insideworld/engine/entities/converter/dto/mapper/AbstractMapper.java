package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.Descriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public abstract class AbstractMapper implements Mapper {

    protected final void write(final Entity entity,
                               final Object target,
                               final Descriptor descriptor) throws ActionException {
        try {
            final Method method = descriptor.getWrite();
            if (method != null) {
                method.invoke(entity, target);
            }
        } catch (final Exception exp) {
            throw new ActionException(
                String.format("Can't write type %s to %s",
                    descriptor.getField().getType(), entity.getClass()),
                exp
            );
        }
    }

    protected final Object read(final Entity entity,
                                final Descriptor descriptor) throws ActionException {
        final Method method = descriptor.getRead();
        final Object target;
        if (method != null) {
            try {
                target = method.invoke(entity);
            } catch (final Exception exp) {
                throw new ActionException(
                    String.format("Can't read type %s from %s",
                        descriptor.getField().getType(), entity.getClass()),
                    exp
                );
            }
        } else {
            target = null;
        }
        return target;
    }

}
