package insideworld.engine.actions.keeper.tags;

public abstract class AbstractTag<T> implements Tag<T> {

    private final String tag;

    public AbstractTag(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }

    @Override
    public boolean equals(Object tag) {
        return this.tag.equals(tag);
    }

    @Override
    public int hashCode() {
        return this.tag.hashCode();
    }
}
