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

package insideworld.engine.core.action.keeper.links;

import com.google.common.collect.Lists;
import insideworld.engine.core.action.chain.Link;
import insideworld.engine.core.action.chain.LinkException;
import insideworld.engine.core.action.keeper.Record;
import insideworld.engine.core.action.keeper.context.Context;
import insideworld.engine.core.action.keeper.output.Output;
import insideworld.engine.core.action.keeper.tags.Tag;
import java.util.Arrays;
import java.util.Collection;
import javax.enterprise.context.Dependent;

/**
 * Check all records in output and filter only provided tags.
 *
 * @since 0.0.1
 */
@Dependent
public class OutputKeep implements Link {

    /**
     * Tags which need keep in output records.
     */
    private final Collection<String> keep = Lists.newLinkedList();

    @Override
    public final void process(final Context context, final Output output) throws LinkException {
        for (final Record record : output) {
            record.values().keySet().retainAll(this.keep);
        }
    }

    /**
     * Add tags which need to keep.
     * @param tag Tags.
     * @return The same instance.
     */
    public final OutputKeep add(final Tag<?>... tag) {
        this.keep.addAll(Arrays.stream(tag).map(Tag::getTag).toList());
        return this;
    }
}
