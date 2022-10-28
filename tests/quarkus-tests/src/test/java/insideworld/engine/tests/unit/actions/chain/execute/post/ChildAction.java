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

package insideworld.engine.tests.unit.actions.chain.execute.post;

import insideworld.engine.actions.Action;
import insideworld.engine.actions.ActionException;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.actions.keeper.output.Output;
import insideworld.engine.tests.unit.actions.chain.execute.TestChainTags;
import javax.inject.Singleton;

/**
 * Child action for test post-execute.
 * Add in context additional tag to test propagation to context.
 * Copy from context UUID tag to output and create 3 empty records.
 * @since 0.14.0
 */
@Singleton
class ChildAction implements Action {

    @Override
    public final void execute(final Context context, final Output output) throws ActionException {
        context.put(TestChainTags.OUTPUT_ADDITIONAL, "ChildAction");
        context.cloneTag(TestChainTags.UUID, output.createRecord());
        output.createRecord();
        output.createRecord();
        output.createRecord();
    }

    @Override
    public final String key() {
        return "insideworld.engine.tests.unit.actions.chain.execute.post.ChildAction";
    }
}