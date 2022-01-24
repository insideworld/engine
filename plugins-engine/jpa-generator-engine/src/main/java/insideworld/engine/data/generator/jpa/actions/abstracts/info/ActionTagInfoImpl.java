package insideworld.engine.data.generator.jpa.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public class ActionTagInfoImpl implements ActionTagInfo {

    private Class<? extends Entity> entity;
    private String key;
    private String tag;
    private Class<?>[] interfaces;

    public ActionTagInfoImpl(final Class<? extends Entity> entity,
                       final String key,
                       final String tag,
                       final Class<?>[] interfaces) {

        this.entity = entity;
        this.key = key;
        this.tag = tag;
        this.interfaces = interfaces;
    }

    @Override
    public Class<? extends Entity> entity() {
        return this.entity;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public Class<?>[] interfaces() {
        return this.interfaces;
    }

    @Override
    public String tag() {
        return this.tag;
    }

}
