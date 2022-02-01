package insideworld.engine.entities.actions;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.actions.links.ReadEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.tags.EntitiesTag;
import insideworld.engine.entities.tags.EntityTag;
import insideworld.engine.entities.tags.StorageTags;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

public abstract class AbstractReadAction<T extends Entity> extends AbstractChainAction {

    public AbstractReadAction(final LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        final LinksBuilder export = builder.addLink(
                new TypeLiteral<ReadEntityLink<T>>() {
                },
                link -> link.setType(this.getType())
                    .setTag(StorageTags.ID, this.getTag())
                    .setTags(StorageTags.IDS, this.getTags()))
            .addLink(ExportEntityLink.class, link -> link
                .setTag(this.getTag())
                .setTag(this.getTags()));
        return this.afterExport(export).build();
    }

    /**
     * @return Tag if read of single entity supported. Null if not.
     */
    protected abstract EntityTag<T> getTag();

    protected abstract EntitiesTag<T> getTags();

    protected abstract Class<T> getType();

    protected LinksBuilder afterExport(final LinksBuilder builder) {
        return builder;
    }

}
