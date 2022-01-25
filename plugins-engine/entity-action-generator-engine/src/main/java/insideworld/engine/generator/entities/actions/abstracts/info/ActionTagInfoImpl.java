package insideworld.engine.generator.entities.actions.abstracts.info;

import insideworld.engine.entities.Entity;

public class ActionTagInfoImpl implements ActionTagInfo {

    private final Class<? extends Entity> entity;
    private final String key;
    private final String tag;
    private final Class<?>[] interfaces;
    private final String implementation;

    public ActionTagInfoImpl(final Class<? extends Entity> entity,
                             final String key,
                             final String tag,
                             final Class<?>[] interfaces,
                             final String implementation) {

        this.entity = entity;
        this.key = key;
        this.tag = tag;
        this.interfaces = interfaces;
        this.implementation = implementation;
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

    @Override
    public String implementation() {
        return this.implementation;
    }

}
