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
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.chain.LinkException;
import insideworld.engine.actions.executor.ActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Arrays;
import java.util.Collection;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Abstract logic for execute action inside chain action.
 * Need to extend realisation to support different key types.
 *
 * @param <T> Key type for execute an action.
 * @since 0.1.0
 */
public abstract class AbstractExecuteActionLink<T> implements Link, ExecuteActionLink<T> {

    /**
     * Executor of action.
     */
    private final ActionExecutor<T> executor;

    /**
     * PreExecute instances.
     */
    private final Collection<PreExecute> pres;

    /**
     * Post execute instances.
     */
    private final Collection<PostExecute> posts;

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
     * Default constructor.
     *
     * @param executor Action executor.
     * @param factory Object factory.
     */
    @Inject
    public AbstractExecuteActionLink(
        final ActionExecutor<T> executor,
        final ObjectFactory factory
    ) {
        this.executor = executor;
        this.factory = factory;
        this.pres = Lists.newLinkedList();
        this.posts = Lists.newLinkedList();
    }

    @Override
    public final void process(final Context parent, final Output output) throws LinkException {
        final Context child;
        if (this.tags == null) {
            child = parent.cloneContext();
        } else {
            child = parent.cloneContext(this.tags);
        }
        if (this.pres.isEmpty() || this.pres.stream().allMatch(pre -> pre.apply(parent, child))) {
            if (this.key == null) {
                throw new LinkException(this, "Action is not set!");
            }
            final Output results;
            try {
                results = this.executor.execute(this.key, child);
            } catch (final ActionException exp) {
                throw new LinkException(this, exp);
            }
            if (this.posts.isEmpty()) {
                output.merge(results);
            } else {
                this.posts.forEach(
                    post -> post.apply(Pair.of(parent, output), Pair.of(child, results))
                );
            }
        }
    }

    @Override
    public final ExecuteActionLink<T> setTags(final Tag<?>... ptags) {
        this.tags = Arrays.copyOf(ptags, ptags.length);
        return this;
    }

    @Override
    public final ExecuteActionLink<T> setKey(final T pkey) {
        this.key = pkey;
        return this;
    }

    @Override
    public final ExecuteActionLink<T> addPreExecute(final PreExecute... executes) {
        this.pres.addAll(Arrays.asList(executes));
        return this;
    }

    @SafeVarargs
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
}
