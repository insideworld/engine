package insideworld.engine.entities.actions;

import insideworld.engine.actions.chain.AbstractChainAction;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinksBuilder;
import insideworld.engine.entities.Entity;
import insideworld.engine.entities.actions.links.WriteEntityLink;
import insideworld.engine.entities.converter.ExportEntityLink;
import insideworld.engine.entities.converter.ImportEntityLink;
import insideworld.engine.entities.tags.EntityTag;
import java.util.Collection;
import javax.enterprise.util.TypeLiteral;

public abstract class AbstractWriteAction<T extends Entity> extends AbstractChainAction {

    public AbstractWriteAction(LinksBuilder builder) {
        super(builder);
    }

    @Override
    protected Collection<Link> attachLinks(final LinksBuilder builder) {
        builder.addLink(ImportEntityLink.class, link -> link.setTag(this.getTag(), this.getType()));
        this.afterImport(builder);
        builder.addLink(
            new TypeLiteral<WriteEntityLink<T>>() {},
            link -> link.setTag(this.getTag()));
        builder.addLink(ExportEntityLink.class, link -> link.setTag(this.getTag()));
        return this.afterExport(builder).build();
    }

    protected abstract EntityTag<T> getTag();

    protected abstract Class<T> getType();

    protected LinksBuilder afterImport(final LinksBuilder builder) {
        return builder;
    }

    protected LinksBuilder afterExport(final LinksBuilder builder) { return builder; }

}
