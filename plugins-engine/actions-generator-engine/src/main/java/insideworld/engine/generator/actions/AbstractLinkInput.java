package insideworld.engine.generator.actions;

import insideworld.engine.actions.keeper.context.Context;

public abstract class AbstractLinkInput implements LinkInput {

    private Context context;

    @Override
    public void setContext(final Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return this.context;
    }

    protected Object readContext(final String tag) {
        return this.context.get(tag);
    }

    protected void writeContext(final String tag, final Object object) {
        this.context.put(tag, object);
    }
}
