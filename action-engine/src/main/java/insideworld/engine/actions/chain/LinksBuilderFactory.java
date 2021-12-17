package insideworld.engine.actions.chain;

import com.google.common.collect.ImmutableList;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.function.Consumer;
import javax.enterprise.context.Dependent;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;

/**
 * Build links using object factory.
 *
 * @since 0.0.6
 */
@Dependent
public class LinksBuilderFactory implements LinksBuilder {

    private final ObjectFactory factory;
    private final ImmutableList.Builder<Link> links = ImmutableList.builder();

    @Inject
    public LinksBuilderFactory(final ObjectFactory factory) {
        this.factory = factory;
    }

    @Override
    public LinksBuilder addLink(final Class<? extends Link> type) {
        final Link link = this.factory.createObject(type);
        this.links.add(link);
        return this;
    }

    @Override
    public <T extends Link> LinksBuilder addLink(final Class<T> type, final Consumer<T> init) {
        final T link = this.factory.createObject(type);
        init.accept(link);
        this.links.add(link);
        return this;
    }

    @Override
    public LinksBuilder addLink(final TypeLiteral<? extends Link> type) {
        final Link link = this.factory.createObject(type);
        this.links.add(link);
        return this;
    }

    @Override
    public <T extends Link> LinksBuilder addLink(
        final TypeLiteral<T> type, final Consumer<T> init) {
        final T link = this.factory.createObject(type);
        init.accept(link);
        this.links.add(link);
        return this;
    }


    @Override
    public Collection<Link> build() {
        return this.links.build();
    }
}
