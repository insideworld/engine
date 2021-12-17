package insideworld.engine.security.common;

import insideworld.engine.actions.keeper.tags.SingleTag;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.security.common.entities.Role;
import insideworld.engine.security.common.entities.User;

public class UserTags {

    private UserTags() {
    }

    public static final EntityTag<User> USER = new EntityTag<>("user");

    public static final EntityTag<User> USER_EXT = new EntityTag<>("userExt");

    public final static EntitiesTag<Role> ROLES = new EntitiesTag<>("roles");

    public final static EntitiesTag<User> USERS_EXT = new EntitiesTag<>("usersExt");

    public final static SingleTag<String> TOKEN = new SingleTag<>("token");

}
