package insideworld.engine.data.jpa;

import insideworld.engine.entities.Entity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractCrudGenericStorage<T extends Entity, C extends T>
    extends AbstractCrudStorage<T, C> {

    private final Class<C> crud;
    private final Class<T> entity;

    public AbstractCrudGenericStorage() {
        final ParameterizedType generics =
            (ParameterizedType) this.getClass().getGenericSuperclass();
        final Type[] arguments = generics.getActualTypeArguments();
        this.crud = (Class<C>) arguments[0];
        this.entity = (Class<T>) arguments[1];
    }

    @Override
    protected final Class<C> forCrud() {
        return this.crud;
    }

    @Override
    public final Class<T> forEntity() {
        return this.entity;
    }

}
