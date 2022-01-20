package insideworld.engine.data.generator.jpa.actions.read.annotations;

import insideworld.engine.entities.Entity;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;

public class ReadActionInfo<T extends Entity> {

    private final EntityTag<T> tag;
    private final EntitiesTag<T> tags;
    private final Class<T> type;

    public ReadActionInfo(final Class<T> type, final EntityTag<T> tag, final EntitiesTag<T> tags) {
        this.type = type;
        this.tag = tag;
        this.tags = tags;
    }

}
