package insideworld.engine.actions.chain;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import java.util.Collection;

/**
 * Execute chain of links.
 */
public abstract class AbstractChainAction implements Action {

    private final Collection<Link> links;

    public AbstractChainAction(final LinksBuilder builder) {
        this.links = this.attachLinks(builder);
    }

    @Override
    public void execute(final Context context, final Output output) throws ActionException {
        for (final Link link : this.links) {
            if (context.contains(ActionsTags.BREAK_CHAIN)) {
                break;
            }
            if (link.can(context)) {
                link.process(context, output);
            }
        }
    }

    /**
     * Collection of links.
     * For build it use LinkBuilder
     * @return
     */
    protected abstract Collection<Link> attachLinks(LinksBuilder builder);

}
