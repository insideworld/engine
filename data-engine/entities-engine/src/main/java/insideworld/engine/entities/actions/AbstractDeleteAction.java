package insideworld.engine.entities.actions;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.actions.links.DeleteEntityLink;
import insideworld.engine.entities.actions.links.ReadEntityLink;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.entities.tags.StorageTags;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

public abstract class AbstractDeleteAction<T extends Entity> extends AbstractChainAction {

    public AbstractDeleteAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        return builder
            .addLink(
                new TypeLiteral<ReadEntityLink<T>>() {
                },
                link -> link.setType(this.getType())
                    .setTag(StorageTags.ID, this.getTag())
                    .setTags(StorageTags.IDS, this.getTags()))
            .addLink(
                new TypeLiteral<DeleteEntityLink<T>>() {
                },
                link -> link
                    .setType(this.getType())
                    .setTag(this.getTag())
                    .setTags(this.getTags()))
            .build();
    }


    protected abstract EntityTag<T> getTag();

    protected abstract EntitiesTag<T> getTags();

    protected abstract Class<T> getType();
}
