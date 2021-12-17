package insideworld.engine.entities.converter.dto;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.converter.dto.mapper.Mapper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

public class Descriptor {

    private final Method read;
    private final Method write;
    private final Field field;

    public Descriptor(final Method read, final Method write, final Field field) {
        this.read = read;
        this.write = write;
        this.field = field;
    }


    public Method getRead() {
        return this.read;
    }

    public Method getWrite() {
        return this.write;
    }

    public Field getField() {
        return this.field;
    }
}
