package insideworld.engine.data.jpa;

import insideworld.engine.entities.Entity;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbstractCrudGenericStorage<T extends Entity, C extends T>
    extends AbstractCrudStorage<T, C> {

    private final Class<C> crud;
    private final Class<T> origin;

    public AbstractCrudGenericStorage() {
        final ParameterizedType generics =
            (ParameterizedType) this.getClass().getGenericSuperclass();
        final Type[] arguments = generics.getActualTypeArguments();
        this.crud = (Class<C>) arguments[0];
        this.origin = (Class<T>) arguments[1];
    }

    @Override
    protected final Class<C> crudType() {
        return this.crud;
    }

    @Override
    protected Class<T> originType() {
        return this.origin;
    }

}
