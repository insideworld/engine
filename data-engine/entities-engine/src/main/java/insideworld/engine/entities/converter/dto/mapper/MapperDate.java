package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.inject.Singleton;

@Singleton
public class MapperDate extends AbstractMapper {

    private final ThreadLocal<SimpleDateFormat> format =
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    @Override
    public void toEntity(Record record, Entity entity, Descriptor descriptor) throws ActionException {
        final Object object = record.get(descriptor.getName());
        final Date date;
        if (Date.class.equals(object.getClass())) {
            date = (Date) object;
        } else {
            try {
                date = this.format.get().parse(object.toString());
            } catch (final ParseException exp) {
                throw new ActionException("Can't parse date", exp);
            }
        }
        this.write(entity, date, descriptor);
    }

    @Override
    public void toRecord(Record record, Entity entity, Descriptor descriptor) throws ActionException {
        final Object target = this.read(entity, descriptor);
        if (target != null) {
            record.put(descriptor.getName(), target);
        }
    }

    @Override
    public boolean canApply(final Descriptor descriptor) {
        return descriptor.getType().equals(Date.class);
    }
}
