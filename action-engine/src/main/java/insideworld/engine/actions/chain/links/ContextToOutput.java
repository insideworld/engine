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

package insideworld.engine.actions.chain.links;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import javax.enterprise.context.Dependent;

@Dependent
public class ContextToOutput implements Link {

    private final Collection<String> tags = Lists.newLinkedList();

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.tags.isEmpty()) {
            addAll(context, output);
        } else {
            addWithTags(context, output);
        }
    }

    private void addWithTags(final Context context, final Output output) {
        final Record record = output.createRecord();
        for (final String tag : this.tags) {
            if (context.contains(tag)) {
                record.put(tag, context.get(tag));
            }
        }
    }

    private void addAll(final Context context, final Output output) {
        output.add(context.clone());
    }

    public ContextToOutput addTag(final Tag<?> tag) {
        this.tags.add(tag.getTag());
        return this;
    }

    public ContextToOutput addTags(final Tag<?>... tags) {
        Arrays.stream(tags).forEach(tag -> this.tags.add(tag.getTag()));
        return this;
    }

    public ContextToOutput addTag(final String tag) {
        this.tags.add(tag);
        return this;
    }

    public ContextToOutput addTags(final String... tags) {
        this.tags.addAll(Arrays.asList(tags));
        return this;
    }

}
