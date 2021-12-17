package insideworld.engine.actions.chain.execute;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.function.Function;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

@Dependent
public class ExecuteActionLink implements Link {

    private final ClassActionExecutor executor;
    private final ObjectFactory factory;

    private Tag<?>[] tags;
    private Class<? extends Action> action;

    private final Collection<PreExecute> pres = Lists.newLinkedList();
    private final Collection<PostExecute> posts = Lists.newLinkedList();

    @Inject
    public ExecuteActionLink(final ClassActionExecutor executor,
                             final ObjectFactory factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        final Context clone = this.tags == null ? context.clone() : context.clone(this.tags);
        if (this.pres.isEmpty()
            || this.pres.stream().allMatch(pre -> pre.apply(clone)
        )) {
            final Output results = this.executor.execute(this.action, clone);
            if (this.posts.isEmpty()
                || this.posts.stream().allMatch(post -> post.apply(clone, results))
            ) {
                output.merge(results);
            }
        }
    }

    public ExecuteActionLink setTags(final Tag<?>... tags) {
        this.tags = tags;
        return this;
    }

    public ExecuteActionLink setAction(final Class<? extends Action> action) {
        this.action = action;
        return this;
    }

    public ExecuteActionLink addPreExecute(final PreExecute execute) {
        this.pres.add(execute);
        return this;
    }

    public ExecuteActionLink addPreExecute(final Class<? extends PreExecute> execute) {
        return this.addPreExecute(this.factory.createObject(execute));
    }

    public ExecuteActionLink addPostExecute(final PostExecute execute) {
        this.posts.add(execute);
        return this;
    }

    public ExecuteActionLink addPostExecute(final Class<? extends PostExecute> execute) {
        return this.addPostExecute(this.factory.createObject(execute));
    }

}
