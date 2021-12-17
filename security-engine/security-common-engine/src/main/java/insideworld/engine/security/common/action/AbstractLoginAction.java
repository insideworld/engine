package insideworld.engine.security.common.action;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.security.common.UserTags;
import java.util.Collection;

public abstract class AbstractLoginAction extends AbstractChainAction implements RoleAction {

    public AbstractLoginAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(LinksBuilder builder) {
        return builder
            .addLink(ExportEntityLink.class, link -> link.setTag(UserTags.USER))
            .build();
    }

    @Override
    public String key() {
        return "login";
    }

}
