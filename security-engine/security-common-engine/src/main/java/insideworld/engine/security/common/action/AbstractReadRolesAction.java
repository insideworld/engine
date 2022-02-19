package insideworld.engine.security.common.action;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.actions.links.ReadEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.tags.StorageTags;
import insideworld.engine.security.common.UserTags;
import insideworld.engine.security.common.entities.Role;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

public abstract class AbstractReadRolesAction extends AbstractChainAction implements RoleAction {

    @Inject
    public AbstractReadRolesAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        return builder
                .addLink(
                        new TypeLiteral<ReadEntityLink<Role>>() {},
                        link -> link.setType(Role.class).setTags(
                                StorageTags.IDS, UserTags.ROLES))
                .addLink(ExportEntityLink.class, link -> link.setTag(UserTags.ROLES))
                .build();
    }

    @Override
    public String key() {
        return "users.roles.read";
    }
}
