package insideworld.engine.entities.converter;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;


public interface EntityConverter {

    /**
     * Map entity to record.
     * @param entity
     * @param record
     * @throws ActionException
     */
    Record convert(Entity entity) throws ActionException;

    /**
     * Map record to entity.
     * @param entity
     * @param record
     * @throws ActionException
     */
    <T extends Entity> T convert(Record record, Class<T> type) throws ActionException;

}
