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

package insideworld.engine.actions;

import insideworld.engine.actions.keeper.tags.SingleTag;

/**
 * Action tags.
 *
 * @since 0.1.0
 */
public final class ActionsTags {

    /**
     * Current action instance.
     */
    public static final SingleTag<Action> ACTION =
        new SingleTag<>("engine.actions.action");

    /**
     * Looks like need to remove it.
     */
    public static final SingleTag<String> FROM_ACTION =
        new SingleTag<>("engine.action.from-action");

    /**
     * Use exists transaction when execute a new action.
     * Default will use the same TX.
     */
    public static final SingleTag<Object> USE_EXIST_TX =
        new SingleTag<>("engine.action.use-exist-tx");

    /**
     * Private constructor.
     */
    private ActionsTags() {
    }
}
