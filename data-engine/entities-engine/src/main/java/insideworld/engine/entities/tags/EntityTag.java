package insideworld.engine.entities.tags;

import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.Entity;

public class EntityTag<T extends Entity> extends SingleTag<T> {

    public EntityTag(final String tag) {
        super(tag);
    }
}
