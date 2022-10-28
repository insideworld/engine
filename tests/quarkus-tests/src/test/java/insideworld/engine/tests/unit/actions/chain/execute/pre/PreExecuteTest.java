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

package insideworld.engine.tests.unit.actions.chain.execute.pre;

import insideworld.engine.actions.chain.execute.PreExecute;
import insideworld.engine.actions.keeper.context.Context;
import insideworld.engine.tests.unit.actions.chain.execute.TestChainTags;
import javax.inject.Singleton;

/**
 * Pre-execute for test.
 * If parent context contains TWO tag - return false and skip execute of child action.
 * If not contains - add this tag and execute child action.
 * @since 0.14.0
 */
@Singleton
public class PreExecuteTest implements PreExecute {
    @Override
    public boolean apply(final Context parent, final Context child) {
        final boolean not = !parent.contains(TestChainTags.TWO);
        if (not) {
            child.put(TestChainTags.TWO, new Object());
        }
        return not;
    }
}
