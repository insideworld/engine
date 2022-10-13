/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions.chain.execute;

import com.google.common.collect.Lists;
import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.ActionsTags;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.facade.impl.ClassActionExecutor;
import insideworld.engine.actions.facade.impl.KeyActionExecutor;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import insideworld.engine.injection.ObjectFactory;
import java.util.Collection;
import java.util.function.Function;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Execute action.
 *
 *
 */
@Dependent
public class ExecuteActionLink implements Link {

    private final ClassActionExecutor cexecutor;
    private final KeyActionExecutor kexecutor;
    private final ObjectFactory factory;

    private Tag<?>[] tags;
    private Class<? extends Action> action;
    private String key;
    private boolean sametx = false;

    private final Collection<PreExecute> pres = Lists.newLinkedList();
    private final Collection<PostExecute> posts = Lists.newLinkedList();

    @Inject
    public ExecuteActionLink(final ClassActionExecutor cexecutor,
                             final KeyActionExecutor kexecutor,
                             final ObjectFactory factory) {
        this.cexecutor = cexecutor;
        this.kexecutor = kexecutor;
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
            if (this.action != null) {
                results = this.cexecutor.execute(this.action, clone);
            } else if (this.key != null) {
                results = this.kexecutor.execute(this.key, clone);
            } else {
                throw new ActionException("Action is not defined");
            }
            if (this.posts.isEmpty()
                || this.posts.stream().allMatch(post -> post.apply(
                    Pair.of(context, clone), Pair.of(output, results)))
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

    public ExecuteActionLink setAction(final String action) {
        this.key = action;
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

    public ExecuteActionLink useSameTx() {
        this.sametx = true;
        return this;
    }

}
