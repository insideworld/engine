package insideworld.engine.security.common;

import insideworld.engine.actions.keeper.tags.MandatoryTag;
import insideworld.engine.actions.keeper.tags.Tag;
import javax.inject.Singleton;

@Singleton
public class UserMandatoryTag implements MandatoryTag {

    @Override
    public Tag<?> get() {
        return UserTags.USER;
    }
}
