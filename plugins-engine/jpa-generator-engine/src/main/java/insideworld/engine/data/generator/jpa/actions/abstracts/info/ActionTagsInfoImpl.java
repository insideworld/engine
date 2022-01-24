package insideworld.engine.data.generator.jpa.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public class ActionTagsInfoImpl extends ActionTagInfoImpl implements ActionTagsInfo {

    private String tags;

    public ActionTagsInfoImpl(final Class<? extends Entity> entity,
                             final String key,
                             final String tag,
                             final String tags,
                             final Class<?>[] interfaces) {
        super(entity, key, tag, interfaces);
        this.tags = tags;
    }

    @Override
    public String tags() {
        return this.tags;
    }
}
