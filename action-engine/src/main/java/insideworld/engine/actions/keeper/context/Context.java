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

package insideworld.engine.actions.keeper.context;

import insideworld.engine.actions.keeper.Record;
import insideworld.engine.actions.keeper.tags.Tag;

/**
 * Context is using to keep information in action.
 * It's basic record with additional clone function.
 *
 * @since 0.0.1
 */
public interface Context extends Record {

    /**
     * Clone with included tags.
     * If includes is null - will copy all.
     * @param includes Tags what need to clone in new context.
     * @return New context.
     */
    Context cloneContext(Tag<?>... includes);

    /**
     * Clone with included tags to provided record.
     * If includes is null - will copy all.
     * @param record Record where need to clone.
     * @param includes Tags what need to clone in new context.
     */
    void cloneToRecord(Record record, String... includes);
}
