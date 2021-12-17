package insideworld.engine.actions.chain;

import java.util.Collection;
import java.util.function.Consumer;
import javax.enterprise.util.TypeLiteral;

public interface LinksBuilder {

    LinksBuilder addLink(Class<? extends Link> type);

    <T extends Link> LinksBuilder addLink(Class<T> type, Consumer<T> init);

    LinksBuilder addLink(TypeLiteral<? extends Link> type);

    <T extends Link> LinksBuilder addLink(TypeLiteral<T> type, Consumer<T> init);

    Collection<Link> build();

}
