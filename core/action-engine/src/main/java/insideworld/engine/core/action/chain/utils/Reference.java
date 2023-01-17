package insideworld.engine.core.action.chain.utils;

import javax.enterprise.context.Dependent;

@Dependent
public class Reference<T> {

    private T reference;

    public Reference() {
    }

    public Reference(final T value) {
        this.reference = value;
    }

    public T getReference() {
        return this.reference;
    }

    public void setReference(final T reference) {
        this.reference = reference;
    }
}
