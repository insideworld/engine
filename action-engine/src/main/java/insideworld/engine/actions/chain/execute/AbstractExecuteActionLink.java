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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Abstract logic for execute action inside chain action.
 * Need to extend realisation to support different key types.
 *
 * @param <T> Key type for execute an action.
 */
public abstract class AbstractExecuteActionLink<T> implements Link, ExecuteActionLink<T> {

    /**
     * Executor of action.
     */
    private final ActionExecutor<T> executor;
    /**
     * PreExecute instances.
     */
    private final Collection<PreExecute> pres = Lists.newLinkedList();
    /**
     * Post execute instances.
     */
    private final Collection<PostExecute> posts = Lists.newLinkedList();
    /**
     * Object factory.
     */
    private final ObjectFactory factory;

    /**
     * Key of action.
     */
    private T key;

    /**
     * Tags to copy from parent context to child.
     */
    private Tag<?>[] tags;

    /**
     * Using the same TX in child action.
     */
    private boolean sametx = false;

    /**
     * Default constructor.
     * @param executor Action executor.
     * @param factory Object factory.
     */
    @Inject
    public AbstractExecuteActionLink(final ActionExecutor<T> executor,
                                     final ObjectFactory factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public final void process(final Context parent, final Output output) throws ActionException {
        final Context child = this.tags == null ? parent.clone() : parent.clone(this.tags);
        if (this.pres.isEmpty() || this.pres.stream().allMatch(pre -> pre.apply(parent, child))) {
            final Output results;
            if (this.sametx) {
                child.put(ActionsTags.USE_EXIST_TX, new Object());
            }
            if (this.key == null) {
                throw new ActionException("Action is not set!");
            }
            results = this.executor.execute(this.key, child);
            if (this.posts.isEmpty()){
                output.merge(results);
            } else {
                this.posts.forEach(post ->
                    post.apply(Pair.of(parent, output), Pair.of(child, results)));
            }
        }
    }

    @Override
    public final ExecuteActionLink<T> setTags(final Tag<?>... tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public final ExecuteActionLink<T> setKey(final T key) {
        this.key = key;
        return this;
    }

    @Override
    public final ExecuteActionLink<T> addPreExecute(final PreExecute... executes) {
        this.pres.addAll(Arrays.asList(executes));
        return this;
    }

    @Override
    public final ExecuteActionLink<T> addPreExecute(final Class<? extends PreExecute>... executes) {
        final PreExecute[] array = Arrays.stream(executes)
            .map(execute -> this.factory.createObject(execute)).toArray(PreExecute[]::new);
        return this.addPreExecute(array);
    }

    @Override
    public final ExecuteActionLink<T> addPostExecute(final PostExecute... executes) {
        this.posts.addAll(Arrays.asList(executes));
        return this;
    }

    @SafeVarargs
    @Override
    public final ExecuteActionLink<T> addPostExecute(
        final Class<? extends PostExecute>... executes) {
        final PostExecute[] array = Arrays.stream(executes)
            .map(execute -> this.factory.createObject(execute)).toArray(PostExecute[]::new);
        return this.addPostExecute(array);
    }

    @Override
    public final ExecuteActionLink<T> useSameTx() {
        this.sametx = true;
        return this;
    }

}