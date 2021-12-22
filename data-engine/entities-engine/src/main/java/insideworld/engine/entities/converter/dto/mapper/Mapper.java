package insideworld.engine.entities.converter.dto.mapper;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.descriptors.Descriptor;

public interface Mapper {

    void toEntity(Record record, Entity entity, Descriptor descriptor)
        throws ActionException;

    void toRecord(Record record, Entity entity, Descriptor descriptor)
        throws ActionException;

    boolean canApply(Descriptor descriptor);

}
