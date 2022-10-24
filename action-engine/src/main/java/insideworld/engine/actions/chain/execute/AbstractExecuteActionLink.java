/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.chain.execute;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Execute actions links.
 * Using to execute another action in chain action.
 * TODO: Write a test.
 *
 * @since 0.1.0
 */

public abstract class AbstractExecuteActionLink<T> implements Link, ExecuteActionLink<T> {

    private final ActionExecutor<T> executor;
    private final Collection<PreExecute> pres = Lists.newLinkedList();
    private final Collection<PostExecute> posts = Lists.newLinkedList();
    private final ObjectFactory factory;

    private T action;
    private Tag<?>[] tags;
    private boolean sametx = false;

    @Inject
    public AbstractExecuteActionLink(final ActionExecutor<T> executor,
                                     final ObjectFactory factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        final Context clone = this.tags == null ? context.clone() : context.clone(this.tags);
        if (this.pres.isEmpty()
            || this.pres.stream().allMatch(pre -> pre.apply(Pair.of(context, clone))
        )) {
            final Output results;
            if (this.sametx) {
                clone.put(ActionsTags.USE_EXIST_TX, new Object());
            }
            if (this.action == null) {
                throw new ActionException("Action is not set!");
            }
            results = this.executor.execute(this.action, clone);
            if (this.posts.isEmpty()
                || this.posts.stream().allMatch(post -> post.apply(
                    Pair.of(context, clone), Pair.of(output, results)))
            ) {
                output.merge(results);
            }
        }
    }

    @Override
    public AbstractExecuteActionLink<T> setTags(final Tag<?>... tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public AbstractExecuteActionLink<T> setAction(final T action) {
        this.action = action;
        return this;
    }

    @Override
    public AbstractExecuteActionLink<T> addPreExecute(final PreExecute execute) {
        this.pres.add(execute);
        return this;
    }

    @Override
    public AbstractExecuteActionLink<T> addPreExecute(final Class<? extends PreExecute> execute) {
        return this.addPreExecute(this.factory.createObject(execute));
    }

    @Override
    public AbstractExecuteActionLink<T> addPostExecute(final PostExecute execute) {
        this.posts.add(execute);
        return this;
    }

    @Override
    public AbstractExecuteActionLink<T> addPostExecute(final Class<? extends PostExecute> execute) {
        return this.addPostExecute(this.factory.createObject(execute));
    }

    @Override
    public AbstractExecuteActionLink<T> useSameTx() {
        this.sametx = true;
        return this;
    }

}
