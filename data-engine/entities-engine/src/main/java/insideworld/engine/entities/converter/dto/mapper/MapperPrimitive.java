package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.Descriptor;
import java.beans.PropertyDescriptor;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class MapperPrimitive extends AbstractMapper {

    @Override
    public void toEntity(final Record record, final Entity entity, final Descriptor descriptor)
        throws ActionException {
        this.write(entity, record.get(descriptor.getField().getName()), descriptor);
    }

    @Override
    public void toRecord(final Record record,
                         final Entity entity,
                         final Descriptor descriptor) throws ActionException {
        final Object target = this.read(entity, descriptor);
        if (target != null) {
            record.put(descriptor.getField().getName(), target);
        }
    }

    @Override
    public boolean canApply(final Descriptor descriptor) {
        final Class<?> type = descriptor.getField().getType();
        return type.isPrimitive() || type.equals(String.class);
    }
}
