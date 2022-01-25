package insideworld.engine.generator.actions;

import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.injection.ObjectFactory;

public abstract class AbstractTypeLink<T extends LinkInput> implements Link {

    private ObjectFactory factory;

    public AbstractTypeLink(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        this.process(this.wrapContext(context), output);
    }

    public abstract void process(T input, Output output);

    protected abstract Class<T> inputType();

    private T wrapContext(final Context context) {
        final T object = this.factory.createObject(this.inputType());
        object.setContext(context);
        return object;
    }
}
