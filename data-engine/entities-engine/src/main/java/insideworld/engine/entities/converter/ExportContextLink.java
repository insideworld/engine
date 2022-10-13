/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.entities.converter;

import com.google.common.collect.ImmutableSet;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.chain.Link;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.actions.keeper.tags.Tag;
import java.util.Collection;
import javax.enterprise.context.Dependent;

/**
 * Link usign to export specific tags from context to output.
 *
 * You need to set in #addTags tags which you want to export in output.
 * Support only single output.
 *
 * @since 0.0.6
 */
@Dependent
public class ExportContextLink implements Link {

    /**
     * Collection of tags which need to export.
     */
    private Collection<Tag<?>> tags;

    @Override
    public void process(final Context context, final Output output) throws ActionException {
        if (this.tags != null) {
            final Record record = output.createRecord();
            for (final Tag<?> tag : this.tags) {
                record.put(tag.getTag(), context.get(tag));
            }
        }
    }

    /**
     * Add export tags.
     * @param tags Tags which need to propagate in output.
     */
    public void addTags(final Collection<Tag<?>> tags) {
        this.tags = ImmutableSet.copyOf(tags);
    }

}
