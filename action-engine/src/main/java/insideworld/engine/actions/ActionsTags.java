/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.actions;

import insideworld.engine.actions.keeper.tags.MultipleTag;
import insideworld.engine.actions.keeper.tags.SingleTag;

public final class ActionsTags {

    private ActionsTags() {
    }

    public static final SingleTag<Action> ACTION = new SingleTag<>("action");

    public static final SingleTag<String> ALIAS = new SingleTag<>("alias");

    public static final SingleTag<Void> EMPTY = new SingleTag<>("empty");

    public static final MultipleTag<Void> EMPTIES = new MultipleTag<>("empty");

    public static final SingleTag<Object> BREAK_CHAIN = new SingleTag<>("breakChain");

    public static final SingleTag<String> BULK = new SingleTag<>("bulk");

    public static final SingleTag<String> FROM_ACTION = new SingleTag<>("action.from-action");

    public static final SingleTag<Object> USE_EXIST_TX = new SingleTag<>("action.use-exist-tx");

}
