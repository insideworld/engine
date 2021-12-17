package insideworld.engine.entities.tags;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.entities.Entity;

public class EntitiesTag<T extends Entity> extends MultipleTag<T> {

    public EntitiesTag(final String tag) {
        super(tag);
    }
}
