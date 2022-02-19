package insideworld.engine.security.common.action;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.actions.links.ReadEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.converter.OutputRemoveLink;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.entities.User;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

/**
 *
 */

public abstract class AbstractReadUsersAction extends AbstractChainAction implements RoleAction {

    @Inject
    public AbstractReadUsersAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        return builder
                .addLink(
                        new TypeLiteral<ReadEntityLink<User>>() {},
                        link -> link.setType(User.class).setTags(StorageTags.IDS, UserTags.USERS_EXT))
                .addLink(ExportEntityLink.class, link -> link.setTag(UserTags.USERS_EXT))
                .addLink(OutputRemoveLink.class, link -> link.add(UserTags.TOKEN))
                .build();
    }

    @Override
    public String key() {
        return "users.users.read";
    }
}
