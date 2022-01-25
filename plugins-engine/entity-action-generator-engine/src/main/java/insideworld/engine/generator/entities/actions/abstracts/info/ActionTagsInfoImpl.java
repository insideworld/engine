package insideworld.engine.generator.entities.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public class ActionTagsInfoImpl extends ActionTagInfoImpl implements ActionTagsInfo {

    private String tags;

    public ActionTagsInfoImpl(final Class<? extends Entity> entity,
                              final String key,
                              final String tag,
                              final String tags,
                              final Class<?>[] interfaces,
                              final String implementation) {
        super(entity, key, tag, interfaces, implementation);
        this.tags = tags;
    }

    @Override
    public String tags() {
        return this.tags;
    }
}
