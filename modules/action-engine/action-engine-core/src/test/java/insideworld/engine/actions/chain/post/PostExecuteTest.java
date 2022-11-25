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

package insideworld.engine.actions.chain.post;

import insideworld.engine.actions.chain.TestChainTags;
import insideworld.engine.actions.chain.execute.PostExecute;
import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import javax.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Post execute tag.
 * Propagate the additional tag to parent context.
 * Create in parent output only one record from child output records which contains UUID tag.
 * @since 0.14.0
 */
@Singleton
public class PostExecuteTest implements PostExecute {
    @Override
    public final void apply(
        final Pair<Context, Output> parent,
        final Pair<Context, Output> child) {
        child.getLeft().cloneTag(TestChainTags.OUTPUT_ADDITIONAL, parent.getLeft());
        for (final Record record : child.getRight()) {
            if (record.contains(TestChainTags.UUID)) {
                record.cloneTag(TestChainTags.UUID, parent.getRight().createRecord());
                break;
            }
        }
    }
}
