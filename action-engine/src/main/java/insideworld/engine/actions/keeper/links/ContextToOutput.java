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

package insideworld.engine.actions.keeper.links;

import com.google.common.collect.Lists;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import javax.enterprise.context.Dependent;

/**
 * Link to propagate some context tags to new record in output.
 * If tags is not setup - will copy whole context with rules of clone context method.
 * @since 0.1.0
 */
@Dependent
public class ContextToOutput implements Link {

    /**
     * Tags to copy.
     */
    private final Collection<String> tags = Lists.newLinkedList();

    @Override
    public final void process(final Context context, final Output output) throws ActionException {
        if (this.tags.isEmpty()) {
            ContextToOutput.addAll(context, output);
        } else {
            this.addWithTags(context, output);
        }
    }

    /**
     * Add tag to copy.
     * @param tag Tag.
     * @return The same instance.
     */
    public final ContextToOutput addTag(final Tag<?> tag) {
        this.tags.add(tag.getTag());
        return this;
    }

    /**
     * Add tags to copy.
     * @param ptags Tags.
     * @return The same instance.
     */
    public final ContextToOutput addTags(final Tag<?>... ptags) {
        Arrays.stream(ptags).forEach(tag -> this.tags.add(tag.getTag()));
        return this;
    }

    /**
     * Add tag by string key.
     * @param tag String key.
     * @return The same instance.
     */
    public final ContextToOutput addTag(final String tag) {
        this.tags.add(tag);
        return this;
    }

    /**
     * Add tags by string keys.
     * @param ptags String keys.
     * @return The same instance.
     */
    public final ContextToOutput addTags(final String... ptags) {
        this.tags.addAll(Arrays.asList(ptags));
        return this;
    }

    /**
     * Add new record with provided tags.
     * @param context Context.
     * @param output Output.
     */
    private void addWithTags(final Context context, final Output output) {
        context.cloneToRecord(output.createRecord(), this.tags.toArray(String[]::new));
    }

    /**
     * Clone all tags from context by context.clone() rules.
     * @param context Context.
     * @param output Output.
     */
    private static void addAll(final Context context, final Output output) {
        output.add(context.cloneContext());
    }

}
